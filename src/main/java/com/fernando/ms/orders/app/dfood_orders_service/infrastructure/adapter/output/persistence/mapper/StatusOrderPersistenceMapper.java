package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.StatusOrder;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.StatusOrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusOrderPersistenceMapper {
    List<StatusOrder> toStatusOrders(List<StatusOrderEntity> statusOrders);
    List<StatusOrderEntity> toStatusOrdersEntity(List<StatusOrder> statusOrders);
}
