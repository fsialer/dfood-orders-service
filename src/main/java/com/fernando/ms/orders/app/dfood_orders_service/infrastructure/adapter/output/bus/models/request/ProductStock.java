package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.bus.models.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock {
    private Long productId;
    private int quantity;
}
