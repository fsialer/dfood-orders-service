package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.StatusOrderRuleException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.StatusOrder;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CompleteOrderStrategy implements IOrderStrategy {
    @Override
    public Order doOperation(Order order) {
        List<String> status=order.getStatusOrders().stream().map(StatusOrder::getStatus).toList();
        if(status.contains(StatusOrderEnum.COMPLETED.name())){
            throw new StatusOrderRuleException("Order already completed.");
        }
        if(!status.contains(StatusOrderEnum.SENT.name())){
            throw new StatusOrderRuleException("Order never was sent.");
        }
        order.setStatusOrder(StatusOrderEnum.COMPLETED);
        return order;
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.COMPLETED.name().equalsIgnoreCase(statusOrder);
    }
}
