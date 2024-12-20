package com.fernando.ms.orders.app.dfood_orders_service.utils;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.OrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderProductEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.StatusOrderEntity;

import java.time.LocalDate;
import java.util.List;

public class TestUtilOrder {

    public static Order buildOrderMock(){
        return Order
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .products(List.of(Product
                        .builder()
                        .id(1L)
                        //.productId(1L)
                        .quantity(2)
                        .price(20.0)
                        //.amount(40.0)
                        .build()))
                .build();
    }

    public static Order buildOrderCanceledMock(){
        return Order
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.CANCELED)
                .products(List.of(Product
                        .builder()
                        .id(1L)
                        //.productId(1L)
                        .quantity(2)
                        .price(20.0)
                        //.amount(40.0)
                        .build()))
                .build();
    }

    public static OrderEntity buildOrderEntityMock(){
        return OrderEntity
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
//                .orderProducts(List.of(OrderProductEntity
//                        .builder()
//                        .id(1L)
//                        .productId(1L)
//                        .quantity(2)
//                        .price(20.0)
//                        .amount(40.0)
//                        .build()))
                .statusOrders(List.of(StatusOrderEntity
                        .builder()
                        .status(StatusOrderEnum.REGISTERED.name())
                        .createAt(LocalDate.now())
                        .build()))
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

    public static CreateOrderRequest buildCreateOrderRequestMock(){
        return CreateOrderRequest
                .builder()
                .userId(1L)
                .totalAmount(40.0)
                .products(
                        List.of(
                                CreateOrderProduct
                                        .builder()
                                        .id(1L)
                                        .price(20.00)
                                        .quantity(2)
                                        .build()
                        )
                )
                .build();
    }

}
