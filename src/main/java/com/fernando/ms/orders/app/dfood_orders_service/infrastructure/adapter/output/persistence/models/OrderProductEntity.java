package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_product")
public class OrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double amount;
    @ManyToOne
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }
}
