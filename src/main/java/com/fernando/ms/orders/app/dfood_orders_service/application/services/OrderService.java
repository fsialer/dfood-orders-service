package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.ExternalProductsInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderBusInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderBusOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.IOrderStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderStrategyException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderInputPort, ExternalProductsInputPort, OrderBusInputPort {

    private final OrderPersistencePort orderPersistencePort;
    private final List<IOrderStrategy> orderStrategyList;
    private final ExternalProductsOutputPort externalProductsOutputPort;
    private final OrderBusOutputPort orderBusOutputPort;

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
        IOrderStrategy orderStrategy=  orderStrategyList.stream()
                .filter(strategy-> strategy.isApplicable(StatusOrderEnum.REGISTERED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        return orderPersistencePort.save(orderStrategy.doOperation(order));
    }

    @Override
    public Order changeStatus(Long id, String status) {
        return orderPersistencePort.findById(id)
                .map(orderUpdate->{
                    return orderPersistencePort.changeStatus(
                            orderStrategyList.stream().filter(strategy-> strategy.isApplicable(status))
                                    .findFirst()
                                    .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + status))
                                    .doOperation(orderUpdate)
                    );
                })
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void verifyExistsProductByIds(Iterable<Long> ids) {
        if(!new HashSet<>(orderPersistencePort.findByIds(ids).stream().map(Order::getId).toList()).containsAll((Collection<?>) ids)) {
            throw new OrderNotFoundException();
        }
    }

    @Override
    public void addProductsToOrder(Long orderId, List<Product> products) {
        externalProductsOutputPort.addProductsToOrder(orderId, products);
    }

    @Override
    public List<Product> findAllProductsByIds(List<Long> ids) {
        return externalProductsOutputPort.findAllProductsByIds(ids);
    }

    @Override
    public void verifyExistProductsByIds(List<Long> ids) {
        externalProductsOutputPort.verifyExistProductsByIds(ids);
    }

    @Override
    public void updateStock(Order order) {
       orderBusOutputPort.updateStock(order);
    }
}
