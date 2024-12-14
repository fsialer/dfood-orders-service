package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
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

    @Override
    public List<Product> findAllProductsByIds(List<Long> ids) {
        return mapper.toProducts(client.findByIds(ids));
    }

    @Override
    public void verifyExistProductsByIds(List<Long> ids) {
        client.verifyExistsByIds(ids);
    }
}
