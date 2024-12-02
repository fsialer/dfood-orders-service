package com.fernando.ms.orders.app.dfood_orders_service.domain.models;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long id;
    private Long user_id;
    private LocalDate dateOrder;
    private Double totalAmount;
    private StatusOrderEnum statusOrder;

}
