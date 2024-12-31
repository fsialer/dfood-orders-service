package com.fernando.ms.orders.app.dfood_orders_service.utils;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.StatusOrder;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.StatusOrderEntity;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public static Order buildOrderMock2(){
        return Order
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .products(new ArrayList<>(List.of(Product
                        .builder()
                        .id(1L)
                        .quantity(2)
                        .price(20.0)
                        .build())))
                .statusOrders(new ArrayList<>(List.of(StatusOrder
                        .builder()
                        .status(StatusOrderEnum.REGISTERED.name())
                        .createAt(LocalDate.now())
                        .build())))
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
                .orderProductList(List.of(OrderProduct
                        .builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(20.0)
                        .amount(40.0)
                        .build()))
                .statusOrders(List.of(StatusOrderEntity
                        .builder()
                        .status(StatusOrderEnum.REGISTERED.name())
                        .createAt(LocalDate.now())
                        .build()))
                .build();
    }

    public static OrderEntity buildOrderEntityMock2(){
         OrderEntity orderEntity=OrderEntity
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .orderProductList(List.of(OrderProduct
                        .builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(20.0)
                        .amount(40.0)
                        .build()))
                .build();
        orderEntity.addStatusOrder();
        return orderEntity;
    }

    public static OrderEntity buildOrderEntityMock5(){
        OrderEntity orderEntity = OrderEntity
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .orderProductList(new ArrayList<>(List.of(
                        OrderProduct.builder()
                                .id(1L)
                                .productId(1L)
                                .quantity(2)
                                .price(20.0)
                                .amount(40.0)
                                .build()
                )))
                .build();
        return orderEntity;
    }

    public static OrderEntity buildOrderEntityMock3(){
        OrderEntity orderEntity = OrderEntity
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.ATTENDED)
                .statusOrders(new ArrayList<>(List.of(
                        StatusOrderEntity
                                .builder()
                                .status(StatusOrderEnum.REGISTERED.name())
                                .createAt(LocalDate.now())
                                .build()
                )))
                .build();

        orderEntity.setOrderProductList(new ArrayList<>(List.of(
                OrderProduct
                        .builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(20.0)
                        .amount(40.0)
                        .build()
        )));

        orderEntity.addStatusOrder();
        return orderEntity;
    }

    public static OrderEntity buildOrderEntityMock4(){
        return OrderEntity
                .builder()
                .id(1L)
                .userId(1L)
                .dateOrder(LocalDate.now())
                .totalAmount(50.00)
                .statusOrder(StatusOrderEnum.REGISTERED)
                .statusOrders(new ArrayList<>(List.of(
                        StatusOrderEntity
                                .builder()
                                .status(StatusOrderEnum.REGISTERED.name())
                                .createAt(LocalDate.now())
                                .build()
                )))
                .orderProductList(new ArrayList<>(List.of(
                        OrderProduct
                                .builder()
                                .id(1L)
                                .productId(1L)
                                .quantity(2)
                                .price(20.0)
                                .amount(40.0)
                                .build()
                )))
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
