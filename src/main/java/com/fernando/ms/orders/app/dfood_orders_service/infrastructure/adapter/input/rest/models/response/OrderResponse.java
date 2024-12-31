package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private LocalDate dateOrder;
    private Double totalAmount;
    private StatusOrderEnum statusOrder;
    private List<ProductResponse> products;

}
