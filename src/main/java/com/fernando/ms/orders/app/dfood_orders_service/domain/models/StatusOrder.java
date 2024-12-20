package com.fernando.ms.orders.app.dfood_orders_service.domain.models;

import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;

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
    private Order order;
}
