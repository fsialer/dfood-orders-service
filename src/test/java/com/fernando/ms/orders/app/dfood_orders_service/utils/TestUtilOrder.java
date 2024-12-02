package com.fernando.ms.orders.app.dfood_orders_service.utils;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;

import java.time.LocalDate;

public class TestUtilOrder {

    public static Order buildOrderMock(){
        return Order
                .builder()
                .id(1L)
                .user_id(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .build();
    }

    public static OrderEntity buildOrderEntityMock(){
        return OrderEntity
                .builder()
                .id(1L)
                .user_id(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .build();
    }

    public static OrderResponse buildOrderResponseMock(){
        return OrderResponse
                .builder()
                .id(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .build();
    }

}
