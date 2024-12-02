package com.fernando.ms.orders.app.dfood_orders_service.application.ports.output;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderPersistencePort {
    List<Order> findAll();
    Optional<Order> findById(Long id);
}
