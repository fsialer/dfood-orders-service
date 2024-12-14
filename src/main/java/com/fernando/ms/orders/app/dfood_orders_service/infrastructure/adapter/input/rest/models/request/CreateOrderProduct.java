package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderProduct {
    private Long id;
    private Integer quantity;
    private Double price;
}
