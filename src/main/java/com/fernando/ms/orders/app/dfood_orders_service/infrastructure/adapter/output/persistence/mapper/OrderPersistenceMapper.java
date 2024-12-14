package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {
    List<Order> toOrders(List<OrderEntity> orders);
    Order toOrder(OrderEntity order);
    OrderEntity toOrderEntity(Order order);
}
