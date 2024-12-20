package com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.externalproduct.ExternalProductStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterOrder implements ExternalProductStrategy {
    private final OrderPersistencePort orderPersistencePort;
    private final ExternalProductsOutputPort externalProductsOutputPort;
    private List<Long> ids;

    @Override
    public Order doOperation(Order order) {
//        try{
            externalProductsOutputPort.verifyExistProductsByIds(ids);
            order.setStatusOrder(StatusOrderEnum.REGISTERED);
            return orderPersistencePort.save(order); //Realizar evento para restar el stock de productos
//        } catch (FeignException e) {
//            System.out.println(e.status());
//            throw new ExternalApiException(e.getMessage(),e.status());
//        }

    }

    @Override
    public boolean isApplicable(String statusOrder) {
        return StatusOrderEnum.REGISTERED.name().equalsIgnoreCase(statusOrder);
    }

    @Override
    public void setIds(List<Long> ids) {
        this.ids=ids;
    }
}
