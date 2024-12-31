package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterOrderStrategy implements IOrderStrategy {
    @Override
    public Order doOperation(Order order) {
        order.setStatusOrder(StatusOrderEnum.REGISTERED);
        return order;
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.REGISTERED.name().equalsIgnoreCase(statusOrder);
    }
}
