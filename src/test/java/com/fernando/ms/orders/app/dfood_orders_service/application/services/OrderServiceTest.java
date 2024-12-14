package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilProduct;
import feign.FeignException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderPersistencePort orderPersistencePort;

    @Mock
    private ExternalProductsOutputPort externalProductsOutputPort;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("When Order Information Exists Expect A List Information Orders")
    void When_OrderInformationExists_Expect_AListInformationOrders(){
        Order order= TestUtilOrder.buildOrderMock();
        when(orderPersistencePort.findAll()).thenReturn(Collections.singletonList(order));
        List<Order> orders=orderService.findAll();
        assertEquals(1,orders.size());
        Mockito.verify(orderPersistencePort,times(1)).findAll();
    }

    @Test
    @DisplayName("When Order Information Not Exists Expect A List Void")
    void When_OrderInformationNotExists_Expect_AListVoid(){
        Order order= TestUtilOrder.buildOrderMock();
        when(orderPersistencePort.findAll()).thenReturn(Collections.emptyList());
        List<Order> orders=orderService.findAll();
        assertEquals(0,orders.size());
        Mockito.verify(orderPersistencePort,times(1)).findAll();
    }

    @Test
    @DisplayName("When Order Information By Identifier Is Correct Expect Order Information Correct")
    void When_OrderInformationByIdentifierIsCorrect_Expect_OrderInformationCorrect(){
        Order order=TestUtilOrder.buildOrderMock();
        when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        Order orderResponse=orderService.findById(1L);
        assertNotNull(orderResponse);
        Mockito.verify(orderPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Expect OrderNotFoundException When Order Information By Identifier Is Incorrect")
    void Expect_OrderNotFoundException_When_OrderInformationByIdentifierIsIncorrect(){
        when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->orderService.findById(1L));
        Mockito.verify(orderPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Expect Order Information Is Correct Expect Order Information To Be Saved")
    void Expect_OrderInformationIsCorrect_Expect_OrderInformationToBeSaved(){
        Order order=TestUtilOrder.buildOrderMock();
        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        doNothing().when(externalProductsOutputPort).verifyExistProductsByIds(anyList());
        Order orderResponse=orderService.save(order);
        assertNotNull(orderResponse);
        Mockito.verify(orderPersistencePort,times(1)).save(any(Order.class));
        Mockito.verify(externalProductsOutputPort,times(1)).verifyExistProductsByIds(anyList());
    }

    @Test
    @DisplayName("Expect FeignException When Product Service Have Not Connection")
    void Expect_FeignException_When_ProductServiceHaveNotConnection(){
        Order order=TestUtilOrder.buildOrderMock();
        doThrow(FeignException.class).when(externalProductsOutputPort).verifyExistProductsByIds(anyList());
        assertThrows(FeignException.class,()->orderService.save(order));
        Mockito.verify(orderPersistencePort,times(0)).save(any(Order.class));
        Mockito.verify(externalProductsOutputPort,times(1)).verifyExistProductsByIds(anyList());
    }
}
