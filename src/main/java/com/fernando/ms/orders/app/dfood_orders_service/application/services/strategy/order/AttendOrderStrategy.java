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
public class AttendOrderStrategy implements IOrderStrategy {
    @Override
    public Order doOperation(Order order) {
        List<String> status=order.getStatusOrders().stream().map(StatusOrder::getStatus).toList();
        if(status.contains(StatusOrderEnum.ATTENDED.name())){
            throw new StatusOrderRuleException("The order is already attended.");
        }
        if(!status.contains(StatusOrderEnum.REGISTERED.name())){
            throw new StatusOrderRuleException("The order must be registered to be attended.");
        }

        order.setStatusOrder(StatusOrderEnum.ATTENDED);
        return order;
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.ATTENDED.name().equalsIgnoreCase(statusOrder);
    }
}
