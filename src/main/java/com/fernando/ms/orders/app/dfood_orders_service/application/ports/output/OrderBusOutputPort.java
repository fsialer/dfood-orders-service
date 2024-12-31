package com.fernando.ms.orders.app.dfood_orders_service.application.ports.output;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

public interface OrderBusOutputPort {
    void updateStock(Order order);
}
