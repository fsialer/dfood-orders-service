package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public List<Order> findAll() {
        return orderPersistenceMapper.toOrders(orderJpaRepository.findAll());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(orderPersistenceMapper::toOrder);
    }
}
