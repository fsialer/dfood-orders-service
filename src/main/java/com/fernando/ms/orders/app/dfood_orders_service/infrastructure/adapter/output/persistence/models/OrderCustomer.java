package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_customer")
@NoArgsConstructor
public class OrderCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    //@Column(name = "order_id")
    private OrderEntity order;
    @Column(name = "customer_id")

    private Long customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCustomer that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId);
    }
}
