package com.fernando.ms.orders.app.dfood_orders_service.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double amount;

}
