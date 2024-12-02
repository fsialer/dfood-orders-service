package com.fernando.ms.orders.app.dfood_orders_service.application.ports.output;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

import java.util.List;

public interface OrderPersistencePort {
    List<Order> findAll();
}
