package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private LocalDate dateOrder;
    private Double totalAmount;
    private StatusOrderEnum statusOrder;
    private LocalDate createAt;
    private LocalDate updateAt;
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OrderProductEntity> orderProducts;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusOrderEntity> statusOrders;

//    public OrderEntity(){
//        this.orderProducts=new ArrayList<>();
//    }
//
//    public void addOrderProduct(OrderProductEntity orderProductEntity){
//        this.orderProducts.add(orderProductEntity);
//    }
}
