package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderInputPort {

    private final OrderPersistencePort orderPersistencePort;

    @Override
    public List<Order> findAll() {
        return orderPersistencePort.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderPersistencePort.findById(id).orElseThrow(OrderNotFoundException::new);
    }


}
