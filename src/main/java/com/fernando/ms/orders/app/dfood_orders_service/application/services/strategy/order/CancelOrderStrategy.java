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
public class CancelOrderStrategy implements IOrderStrategy {
    @Override
    public Order doOperation(Order order) {
        List<String> status=order.getStatusOrders().stream().map(StatusOrder::getStatus).toList();

        if(status.contains(StatusOrderEnum.ATTENDED.name())||status.contains(StatusOrderEnum.SENT.name())||status.contains(StatusOrderEnum.COMPLETED.name())){
            throw new StatusOrderRuleException("Order can't be canceled.");
        }
        order.setStatusOrder(StatusOrderEnum.CANCELED);
        return order;
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.CANCELED.name().equalsIgnoreCase(statusOrder);
    }
}
