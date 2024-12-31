package com.fernando.ms.orders.app.dfood_orders_service.application.ports.input;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

public interface OrderBusInputPort {
    void updateStock(Order order);
}
