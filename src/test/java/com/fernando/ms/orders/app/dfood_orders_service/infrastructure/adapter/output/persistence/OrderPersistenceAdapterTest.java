package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderPersistenceAdapterTest {
    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private OrderPersistenceMapper orderPersistenceMapper;

    @InjectMocks
    private OrderPersistenceAdapter orderPersistenceAdapter;

    @Test
    @DisplayName("When Order Information Exists Expect A List Orders Availability")
    void When_OrderInformationExists_Expect_AListOrdersAvailability(){
        OrderEntity orderEntity= TestUtilOrder.buildOrderEntityMock();
        Order order=TestUtilOrder.buildOrderMock();
        when(orderJpaRepository.findAll()).thenReturn(Collections.singletonList(orderEntity));
        when(orderPersistenceMapper.toOrders(anyList())).thenReturn(Collections.singletonList(order));

        List<Order> orders=orderPersistenceAdapter.findAll();
        assertEquals(1,orders.size());
        Mockito.verify(orderJpaRepository,times(1)).findAll();
        Mockito.verify(orderPersistenceMapper,times(1)).toOrders(anyList());
    }

    @Test
    @DisplayName("When Order Information Not Exists Expect A List Void")
    void When_OrderInformationNotExists_Expect_AListVoid(){
        OrderEntity orderEntity= TestUtilOrder.buildOrderEntityMock();
        Order order=TestUtilOrder.buildOrderMock();
        when(orderJpaRepository.findAll()).thenReturn(Collections.emptyList());
        when(orderPersistenceMapper.toOrders(anyList())).thenReturn(Collections.emptyList());

        List<Order> orders=orderPersistenceAdapter.findAll();
        assertEquals(0,orders.size());
        Mockito.verify(orderJpaRepository,times(1)).findAll();
        Mockito.verify(orderPersistenceMapper,times(1)).toOrders(anyList());
    }

    @Test
    @DisplayName("When Order Information By Identifier Is Correct Expect Order Information Correct")
    void When_OrderInformationByIdentifierIsCorrect_Expect_OrderInformationCorrect(){
        OrderEntity orderEntity= TestUtilOrder.buildOrderEntityMock();
        Order order=TestUtilOrder.buildOrderMock();
        when(orderJpaRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderPersistenceMapper.toOrder(any(OrderEntity.class))).thenReturn(order);
        Optional<Order> orderResponse=orderPersistenceAdapter.findById(1L);
        assertNotNull(orderResponse);
        Mockito.verify(orderJpaRepository,times(1)).findById(anyLong());
        Mockito.verify(orderPersistenceMapper,times(1)).toOrder(any(OrderEntity.class));
    }

    @Test
    @DisplayName("When Order Information Is Correct Expect Order Information To Be Saved")
    void When_OrderInformationIsCorrect_Expect_OrderInformationToBeSaved(){
        OrderEntity orderEntity= TestUtilOrder.buildOrderEntityMock();
        Order order=TestUtilOrder.buildOrderMock();
        when(orderJpaRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderPersistenceMapper.toOrder(any(OrderEntity.class))).thenReturn(order);
        when(orderPersistenceMapper.toOrderEntity(any(Order.class))).thenReturn(orderEntity);
        Order orderResponse=orderPersistenceAdapter.save(order);
        assertNotNull(orderResponse);
        Mockito.verify(orderJpaRepository,times(1)).save(any(OrderEntity.class));
        Mockito.verify(orderPersistenceMapper,times(1)).toOrder(any(OrderEntity.class));
        Mockito.verify(orderPersistenceMapper,times(1)).toOrderEntity(any(Order.class));
    }


}
