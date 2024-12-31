package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.StatusOrderRuleException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.StatusOrder;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendOrderStrategy implements IOrderStrategy {
    @Override
    public Order doOperation(Order order) {
        List<String> status=order.getStatusOrders().stream().map(StatusOrder::getStatus).toList();
        if(status.contains(StatusOrderEnum.SENT.name())){
            throw new StatusOrderRuleException("The order has already been sent.");
        }
        if(!status.contains(StatusOrderEnum.ATTENDED.name())){
            throw new StatusOrderRuleException("The order must be attended before being sent.");
        }
        order.setStatusOrder(StatusOrderEnum.SENT);
        return order;
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.SENT.name().equalsIgnoreCase(statusOrder);
    }
}
