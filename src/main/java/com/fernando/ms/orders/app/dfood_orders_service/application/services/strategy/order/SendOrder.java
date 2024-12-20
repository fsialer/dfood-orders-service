package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendOrder implements IOrderStrategy {
    private final OrderPersistencePort orderPersistencePort;
    @Override
    public Order doOperation(Order order) {
        order.setStatusOrder(StatusOrderEnum.SENT);
        return orderPersistencePort.save(order);
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.SENT.name().equalsIgnoreCase(statusOrder);
    }
}
