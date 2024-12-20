package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderProductPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.StatusOrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;
    private final OrderProductPersistenceMapper orderProductPersistenceMapper;

    @Override
    public List<Order> findAll() {
        return orderPersistenceMapper.toOrders(orderJpaRepository.findAll());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(orderPersistenceMapper::toOrder);
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity=orderPersistenceMapper.toOrderEntity(order);
        orderEntity.setStatusOrders(List
                .of(StatusOrderEntity
                        .builder()
                        .order(orderEntity)
                        .createAt(LocalDate.now())
                        .status(orderEntity.getStatusOrder().name())
                        .build()
                ));
        return orderPersistenceMapper.toOrder(orderJpaRepository.save(orderEntity));
    }
}
