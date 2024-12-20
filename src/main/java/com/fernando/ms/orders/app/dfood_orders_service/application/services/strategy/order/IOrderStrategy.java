package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;

public interface IOrderStrategy {

    Order doOperation(Order order);
    boolean isApplicable(String statusOrder);
}
