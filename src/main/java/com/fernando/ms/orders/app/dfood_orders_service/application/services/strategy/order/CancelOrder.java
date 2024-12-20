package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CancelOrder implements IOrderStrategy {
    private final OrderPersistencePort orderPersistencePort;
    @Override
    public Order doOperation(Order order) {
        order.setStatusOrder(StatusOrderEnum.CANCELED);
        return orderPersistencePort.save(order); //Realizar evento para restaurar el stock de productos
    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.CANCELED.name().equalsIgnoreCase(statusOrder);
    }
}
