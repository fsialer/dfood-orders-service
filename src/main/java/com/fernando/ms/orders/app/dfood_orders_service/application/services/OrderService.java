package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.externalproduct.ExternalProductStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.IOrderStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderStrategyException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderInputPort {

    private final OrderPersistencePort orderPersistencePort;
    //private final ExternalProductsOutputPort externalProductsOutputPort;
    private final List<IOrderStrategy> orderStrategyList;

    @Override
    public List<Order> findAll() {
        return orderPersistencePort.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderPersistencePort.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(Order order) {
        List<Long> productsIds=order.getProducts()
                .stream()
                .map(Product::getId)
                .toList();

        IOrderStrategy orderStrategy=  orderStrategyList.stream()
                .filter(strategy-> strategy.isApplicable(StatusOrderEnum.REGISTERED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));
        if (orderStrategy instanceof ExternalProductStrategy externalStrategy) {
            externalStrategy.setIds(productsIds);
        }
        return orderStrategy.doOperation(order);
    }

    @Override
    public Order changeStatus(Long id, String status) {
        return orderPersistencePort.findById(id)
                .map(orderUpdate->{
                    return orderStrategyList.stream().filter(strategy-> strategy.isApplicable(status))
                            .findFirst()
                            .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + status))
                            .doOperation(orderUpdate);
                })
                .orElseThrow(OrderNotFoundException::new);
    }

}
