package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.externalproduct.ExternalProductStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.IOrderStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.RegisterOrder;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderStrategyException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
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
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderPersistencePort orderPersistencePort;

//    @Mock
//    private ExternalProductsOutputPort externalProductsOutputPort;
    @InjectMocks
    private OrderService orderService;

//    @Mock
//    private IOrderStrategy externalProductStrategy;

    @Mock
    private IOrderStrategy orderStrategy;

    @Mock
    private  ExternalProductStrategy externalStrategy;

    private List<IOrderStrategy> orderStrategyList;

    @Mock
    private IOrderStrategy externalProductStrategy;

    @Mock
    private IOrderStrategy anotherStrategy;

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
    @DisplayName("When Order Information Is Correct Expect Order Information To Be Saved")
    void When_OrderInformationIsCorrect_Expect_OrderInformationToBeSaved(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        Order order=TestUtilOrder.buildOrderMock();
        List<Long> productsIds = List.of(1L,2L);
        when(externalProductStrategy.isApplicable(StatusOrderEnum.REGISTERED.name())).thenReturn(true);
        when(anotherStrategy.isApplicable(anyString())).thenReturn(false);
        //when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);

        IOrderStrategy orderStrategy = orderStrategyList.stream()
                .filter(strategy -> strategy.isApplicable(StatusOrderEnum.REGISTERED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        if (orderStrategy instanceof ExternalProductStrategy externalProductStrategy) {
            externalProductStrategy.setIds(productsIds);
        }

        orderStrategy.doOperation(order);

        assertNotNull(orderStrategy);
        Mockito.verify(orderStrategy,times(1)).doOperation(order);
        Mockito.verify(orderStrategy,times(1)).isApplicable(anyString());
    }

    @Test
    @DisplayName("Expect OrderStrategyException When StatusOrderIsInvalid")
    void Expect_OrderStrategyException_When_StatusOrderIsInvalid(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        Order order=TestUtilOrder.buildOrderMock();
        when(anotherStrategy.isApplicable(StatusOrderEnum.REGISTERED.name())).thenReturn(false);

        OrderStrategyException exception=assertThrows(OrderStrategyException.class, () -> {
            orderStrategyList.stream()
                    .filter(strategy -> strategy.isApplicable(StatusOrderEnum.REGISTERED.name()))
                    .findFirst()
                    .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));
        });

        assertEquals("No order strategy found for status type: REGISTERED", exception.getMessage());
        Mockito.verify(anotherStrategy,times(0)).doOperation(order);
        Mockito.verify(anotherStrategy,times(1)).isApplicable(anyString());
    }

    @Test
    @DisplayName("When OrderCanceledIsCorrect Expect OrderChangeStatus")
    void When_OrderCanceledIsCorrect_Expect_OrderChangeStatus(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        //Order orderCanceled=TestUtilOrder.buildOrderCanceledMock();
        Order order=TestUtilOrder.buildOrderMock();
       // when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(externalProductStrategy.isApplicable(StatusOrderEnum.CANCELED.name())).thenReturn(true);
        when(anotherStrategy.isApplicable(anyString())).thenReturn(false);
        //when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);

        IOrderStrategy orderStrategy = orderStrategyList.stream()
                .filter(strategy -> strategy.isApplicable(StatusOrderEnum.CANCELED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        orderStrategy.doOperation(order);

        assertNotNull(orderStrategy);
        Mockito.verify(orderStrategy,times(1)).doOperation(order);
        Mockito.verify(orderStrategy,times(1)).isApplicable(anyString());
    }


    @Test
    @DisplayName("When OrderAttendedIsCorrect Expect OrderChangeStatus")
    void When_OrderAttendedIsCorrect_Expect_OrderChangeStatus(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        //Order orderCanceled=TestUtilOrder.buildOrderCanceledMock();
        Order order=TestUtilOrder.buildOrderMock();
        // when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(externalProductStrategy.isApplicable(StatusOrderEnum.ATTENDED.name())).thenReturn(true);
        when(anotherStrategy.isApplicable(anyString())).thenReturn(false);
        //when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);

        IOrderStrategy orderStrategy = orderStrategyList.stream()
                .filter(strategy -> strategy.isApplicable(StatusOrderEnum.ATTENDED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        orderStrategy.doOperation(order);

        assertNotNull(orderStrategy);
        Mockito.verify(orderStrategy,times(1)).doOperation(order);
        Mockito.verify(orderStrategy,times(1)).isApplicable(anyString());
    }

    @Test
    @DisplayName("When OrderSentIsCorrect Expect OrderChangeStatus")
    void When_OrderSentIsCorrect_Expect_OrderChangeStatus(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        //Order orderCanceled=TestUtilOrder.buildOrderCanceledMock();
        Order order=TestUtilOrder.buildOrderMock();
        // when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(externalProductStrategy.isApplicable(StatusOrderEnum.SENT.name())).thenReturn(true);
        when(anotherStrategy.isApplicable(anyString())).thenReturn(false);
        //when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);

        IOrderStrategy orderStrategy = orderStrategyList.stream()
                .filter(strategy -> strategy.isApplicable(StatusOrderEnum.SENT.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        orderStrategy.doOperation(order);

        assertNotNull(orderStrategy);
        Mockito.verify(orderStrategy,times(1)).doOperation(order);
        Mockito.verify(orderStrategy,times(1)).isApplicable(anyString());
    }

    @Test
    @DisplayName("When OrderCompletedIsCorrect Expect OrderChangeStatus")
    void When_OrderCompletedIsCorrect_Expect_OrderChangeStatus(){
        orderStrategyList = List.of(anotherStrategy, externalProductStrategy);
        //Order orderCanceled=TestUtilOrder.buildOrderCanceledMock();
        Order order=TestUtilOrder.buildOrderMock();
        // when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(externalProductStrategy.isApplicable(StatusOrderEnum.COMPLETED.name())).thenReturn(true);
        when(anotherStrategy.isApplicable(anyString())).thenReturn(false);
        //when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);

        IOrderStrategy orderStrategy = orderStrategyList.stream()
                .filter(strategy -> strategy.isApplicable(StatusOrderEnum.COMPLETED.name()))
                .findFirst()
                .orElseThrow(() -> new OrderStrategyException("No order strategy found for status type: " + order.getStatusOrder().name()));

        orderStrategy.doOperation(order);

        assertNotNull(orderStrategy);
        Mockito.verify(orderStrategy,times(1)).doOperation(order);
        Mockito.verify(orderStrategy,times(1)).isApplicable(anyString());
    }
}
