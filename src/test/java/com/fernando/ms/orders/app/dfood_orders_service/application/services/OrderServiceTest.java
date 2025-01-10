package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalCustomersOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderBusOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.application.services.strategy.order.IOrderStrategy;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderStrategyException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.enums.StatusOrderEnum;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderPersistencePort orderPersistencePort;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private IOrderStrategy orderStrategy;

    private List<IOrderStrategy> orderStrategyList;

    @Mock
    private ExternalProductsOutputPort externalProductsOutputPort;

    @Mock
    ExternalCustomersOutputPort externalCustomersOutputPort;

    @Mock
    private OrderBusOutputPort orderBusOutputPort;

    @BeforeEach
    void setUp() {
        orderStrategyList = List.of(orderStrategy);
        //orderService = new OrderService(orderPersistencePort, orderStrategyList,externalProductsOutputPort,externalCustomersOutputPort,orderBusOutputPort);
        orderService = new OrderService(orderPersistencePort, orderStrategyList,externalProductsOutputPort,externalCustomersOutputPort);
    }

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
        Order order = TestUtilOrder.buildOrderMock();

        when(orderStrategy.isApplicable(StatusOrderEnum.REGISTERED.name())).thenReturn(true);
        when(orderStrategy.doOperation(any(Order.class))).thenReturn(order);
        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        doNothing().when(externalCustomersOutputPort).verifyExistsById(anyLong());

        Order savedOrder = orderService.save(order);

        assertNotNull(savedOrder);
        verify(orderStrategy, times(1)).isApplicable(StatusOrderEnum.REGISTERED.name());
        verify(orderStrategy, times(1)).doOperation(order);
        verify(externalCustomersOutputPort, times(1)).verifyExistsById(anyLong());
        verify(orderPersistencePort, times(1)).save(order);
    }

    @Test
    @DisplayName("Expect OrderStrategyException When StatusOrderIsInvalid")
    void Expect_OrderStrategyException_When_StatusOrderIsInvalid(){
        Order order = TestUtilOrder.buildOrderMock();

        when(orderStrategy.isApplicable(StatusOrderEnum.REGISTERED.name())).thenReturn(false);

        OrderStrategyException exception = assertThrows(OrderStrategyException.class, () -> {
            orderService.save(order);
        });

        assertEquals("No order strategy found for status type: REGISTERED", exception.getMessage());
        verify(orderStrategy, times(1)).isApplicable(StatusOrderEnum.REGISTERED.name());
        verify(orderStrategy, times(0)).doOperation(order);
        verify(orderPersistencePort, times(0)).save(order);
    }

    @Test
    @DisplayName("When Order Status Is Changed Correctly Expect Order To Be Updated")
    void When_OrderStatusIsChangedCorrectly_Expect_OrderToBeUpdated() {
        Order order = TestUtilOrder.buildOrderMock();
        String newStatus = StatusOrderEnum.ATTENDED.name();

        when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderStrategy.isApplicable(newStatus)).thenReturn(true);
        when(orderStrategy.doOperation(order)).thenReturn(order);
        when(orderPersistencePort.changeStatus(order)).thenReturn(order);

        Order updatedOrder = orderService.changeStatus(1L, newStatus);

        assertNotNull(updatedOrder);
        verify(orderPersistencePort, times(1)).findById(anyLong());
        verify(orderStrategy, times(1)).isApplicable(newStatus);
        verify(orderStrategy, times(1)).doOperation(order);
        verify(orderPersistencePort, times(1)).changeStatus(order);
    }

    @Test
    @DisplayName("Expect OrderNotFoundException When Order Does Not Exist")
    void Expect_OrderNotFoundException_When_OrderDoesNotExist() {
        when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.changeStatus(1L, StatusOrderEnum.ATTENDED.name()));

        verify(orderPersistencePort, times(1)).findById(anyLong());
        verify(orderStrategy, times(0)).isApplicable(anyString());
        verify(orderStrategy, times(0)).doOperation(any(Order.class));
        verify(orderPersistencePort, times(0)).changeStatus(any(Order.class));
    }

    @Test
    @DisplayName("Expect OrderStrategyException When No Applicable Strategy Found")
    void Expect_OrderStrategyException_When_NoApplicableStrategyFound() {
        Order order = TestUtilOrder.buildOrderMock();
        String newStatus = StatusOrderEnum.ATTENDED.name();

        when(orderPersistencePort.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderStrategy.isApplicable(newStatus)).thenReturn(false);

        assertThrows(OrderStrategyException.class, () -> orderService.changeStatus(1L, newStatus));

        verify(orderPersistencePort, times(1)).findById(anyLong());
        verify(orderStrategy, times(1)).isApplicable(newStatus);
        verify(orderStrategy, times(0)).doOperation(any(Order.class));
        verify(orderPersistencePort, times(0)).changeStatus(any(Order.class));
    }

    @Test
    @DisplayName("When Orders Identifiers Are Corrects Expect A Return True")
    void When_OrdersIdentifiersAreCorrect_Expect_AReturnTrue() {
        List<Long> ids = Collections.singletonList(1L);
        when(orderPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtilOrder.buildOrderMock()));
        orderService.verifyExistsByIds(ids);
        Mockito.verify(orderPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("Expect OrderNotFoundException When Orders Identifiers Are Incorrect")
    void Expect_OrderNotFoundException_WhenOrdersIdentifiersAreIncorrect() {
        List<Long> ids = Collections.singletonList(2L);
        when(orderPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtilOrder.buildOrderMock()));
        assertThrows(OrderNotFoundException.class,()->orderService.verifyExistsByIds(ids));
        Mockito.verify(orderPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("When Orders Identifiers Of Status Are Corrects Expect A Return True")
    void When_OrdersIdentifiersOfStatusAreCorrect_Expect_AReturnTrue() {
        List<Long> ids = Collections.singletonList(1L);
        when(orderPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtilOrder.buildOrderMock()));
        orderService.verifyExistsStatusByIds(ids,"REGISTERED");
        Mockito.verify(orderPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("Expect OrderNotFoundException When Orders Identifiers Of Status Are Incorrect")
    void Expect_OrderNotFoundException_WhenOrdersIdentifiersOfStatusAreIncorrect() {
        List<Long> ids = Collections.singletonList(2L);
        when(orderPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtilOrder.buildOrderMock()));
        assertThrows(OrderNotFoundException.class,()->orderService.verifyExistsStatusByIds(ids,"REGISTERED"));
        Mockito.verify(orderPersistencePort, times(1)).findByIds(anyCollection());
    }
}
