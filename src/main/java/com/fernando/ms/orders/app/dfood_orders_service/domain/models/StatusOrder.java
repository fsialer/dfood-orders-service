package com.fernando.ms.orders.app.dfood_orders_service.domain.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusOrder {
    private Long id;
    private String status;
    private LocalDate createAt;
    //private Order order;
}
