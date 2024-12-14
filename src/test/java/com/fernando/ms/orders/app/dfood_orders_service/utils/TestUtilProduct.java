package com.fernando.ms.orders.app.dfood_orders_service.utils;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;

public class TestUtilProduct {
    public static Product buildProductMock(){
        return Product
                .builder()
                .id(1L)
                .name("Polo")
                .description("POLO XL")
                .quantity(2)
                .price(20.00)
                .build();
    }

    public static ProductClientResponse buildProductResponseMock(){
        return ProductClientResponse
                .builder()
                .id(1L)
                .name("Polo")
                .description("POLO XL")
                .quantity(2)
                .price(20.00)
                .build();
    }



}
