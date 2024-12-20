package com.fernando.ms.orders.app.dfood_orders_service.application.ports.input;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

import java.util.List;

public interface OrderInputPort {
    List<Order> findAll();
    Order findById(Long id);
    Order save(Order order);
    Order changeStatus(Long id,String status);
}
 