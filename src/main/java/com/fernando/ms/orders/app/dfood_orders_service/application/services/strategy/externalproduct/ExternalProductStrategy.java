package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.externalproduct;

import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.IOrderStrategy;

import java.util.List;

public interface ExternalProductStrategy extends IOrderStrategy {
    void setIds(List<Long> ids);
}
