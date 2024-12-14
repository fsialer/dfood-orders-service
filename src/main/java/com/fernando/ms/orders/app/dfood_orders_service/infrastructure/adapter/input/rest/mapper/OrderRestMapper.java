package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderRestMapper {

    List<OrderResponse> toOrdersResponse(List<Order> orders);
    OrderResponse toOrderResponse(Order order);
    @Mapping(target = "dateOrder", expression = "java(mapDateOrder())")
    @Mapping(target = "statusOrder", expression = "java(mapStatusOrderRegistered())")
    Order toOrder(CreateOrderRequest createOrderRequest);

    default LocalDate mapDateOrder() {
        return LocalDate.now();
    }

    default StatusOrderEnum mapStatusOrderRegistered(){
        return StatusOrderEnum.REGISTERED;
    }


}
