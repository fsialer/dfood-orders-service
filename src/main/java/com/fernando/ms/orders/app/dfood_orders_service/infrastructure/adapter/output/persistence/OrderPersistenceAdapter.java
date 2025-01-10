package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.StatusOrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;
    private final ProductFeignClient client;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final StatusOrderPersistenceMapper statusOrderPersistenceMapper;

    @Override
    public List<Order> findAll() {
        return orderPersistenceMapper.toOrders(orderJpaRepository.findAll());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id)
                .map(orderEntity -> {
                    List<Long> productsId=orderEntity.getOrderProductList()
                            .stream()
                            .map(OrderProduct::getProductId)
                            .toList();
                    List<Product> productsClient=productPersistenceMapper.toProducts(client.findByIds(productsId));
                    List<Product> products=productPersistenceMapper.toProducts2(orderEntity.getOrderProductList());
                    products.forEach(product -> {
                        productsClient.stream().filter(productClient -> productClient.getId().equals(product.getId())).forEach(productClient -> {
                            product.setName(productClient.getName());
                            product.setDescription(productClient.getDescription());
                        });
                    });

                    Order order=orderPersistenceMapper.toOrder(orderEntity);
                    order.setProducts(products);
                    order.setStatusOrders(statusOrderPersistenceMapper.toStatusOrders(orderEntity.getStatusOrders()));
                    return order;
                });
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity=orderPersistenceMapper.toOrderEntity(order);
        orderEntity.addStatusOrder();
        orderEntity.setOrderCustomerId(order.getCustomer().getId());
        return orderPersistenceMapper.toOrder(orderJpaRepository.save(orderEntity));
    }

    @Override
    public Order changeStatus(Order order) {
        OrderEntity orderEntity=orderPersistenceMapper.toOrderEntity(order);
        OrderEntity orderEntity2=orderJpaRepository.findById(order.getId()).get();
        orderEntity.setOrderProductList(
                new ArrayList<>(orderJpaRepository.findById(order.getId()).get().getOrderProductList())
        );
        orderEntity.setStatusOrders(
                new ArrayList<>(orderJpaRepository.findById(order.getId()).get().getStatusOrders()));
        orderEntity.addStatusOrder();
        orderEntity.setCustomer(orderEntity2.getCustomer());
        orderEntity.setCreatedAt(orderEntity2.getCreatedAt());
        return orderPersistenceMapper.toOrder(orderJpaRepository.save(orderEntity));
    }

    @Override
    public List<Order> findByIds(Iterable<Long> ids) {
        return orderPersistenceMapper.toOrders(orderJpaRepository.findAllById(ids));
    }


}
