package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(name = "user_id")
//    private Long userId;
    private LocalDateTime dateOrder;
    private Double totalAmount;
    private StatusOrderEnum statusOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusOrderEntity> statusOrders=new ArrayList<>();;

    @JoinColumn(name = "order_id")
    //@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList=new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderCustomer customer;

    public void addStatusOrder(){
        if (this.statusOrders == null) {
            this.statusOrders = new ArrayList<>();
        }
        this.statusOrders.add(StatusOrderEntity
                .builder()
                        .order(this)
                        .status(this.getStatusOrder().name())
                        .createAt(LocalDateTime.now())
                .build());
    }

    public void addOrderProduct(OrderProduct orderProduct){
        if (this.orderProductList == null) {
            this.orderProductList = new ArrayList<>();
        }
        this.orderProductList.add(orderProduct);
    }

    public void setOrderCustomerId(Long customerId){
        this.customer = OrderCustomer
                .builder()
                .customerId(customerId)
                .order(this)
                .build();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
