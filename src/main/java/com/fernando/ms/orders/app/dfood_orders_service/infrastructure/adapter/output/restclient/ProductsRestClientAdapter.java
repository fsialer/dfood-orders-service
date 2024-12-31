package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.ProductFeignClient;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.mapper.ProductRestClientMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductsRestClientAdapter implements ExternalProductsOutputPort {

    private final ProductFeignClient client;
    private final ProductRestClientMapper mapper;
    private final OrderJpaRepository repository;

    @Override
    public void addProductsToOrder(Long orderId, List<Product> products) {
        repository.findById(orderId)
                .map(order -> {
                    List<Long> ids = products.stream().map(Product::getId).toList();
                    client.verifyExistsByIds(ids);
                    for (Product product : products) {
                        order.addOrderProduct(OrderProduct.builder()
                                .productId(product.getId())
                                .quantity(product.getQuantity())
                                        .price(product.getPrice())
                                        .amount(product.getQuantity()*product.getPrice())
                                .build());
                    }
                    repository.save(order);
                    return products;
                })
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<Product> findAllProductsByIds(List<Long> ids) {
        return mapper.toProducts(client.findByIds(ids));
    }

    @Override
    public void verifyExistProductsByIds(List<Long> ids) {
        client.verifyExistsByIds(ids);
    }
}
