package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity,Long> {
}
