package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderRestMapper {

    List<OrderResponse> toOrdersResponse(List<Order> orders);
}
