package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductClientResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
