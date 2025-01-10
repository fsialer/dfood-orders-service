package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "status_orders")
public class StatusOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDateTime createAt;
    @ManyToOne
    private OrderEntity order;
}
