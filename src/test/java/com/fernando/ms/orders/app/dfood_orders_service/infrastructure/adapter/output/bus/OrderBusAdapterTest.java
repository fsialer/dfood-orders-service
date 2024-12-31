package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.bus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderBusAdapterTest {
    @Mock
    private ServiceBusSenderClient sender;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderBusAdapter orderBusAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("When Order is Correct Expect Update Stock Successfully")
    void When_OrderIsCorrect_Expect_UpdateStockSuccessfully() throws JsonProcessingException {
        //List<ProductStock> productStockList = List.of(new ProductStock(1L, 10));
        String messageContent = "[{\"productId\":1,\"quantity\":5},{\"productId\":2,\"quantity\":8}]";
        Order order=TestUtilOrder.buildOrderMock2();

        when(objectMapper.writeValueAsString(any())).thenReturn(messageContent);

        orderBusAdapter.updateStock(order);

        verify(objectMapper, times(1)).writeValueAsString(any());
        verify(sender, times(1)).sendMessage(any(ServiceBusMessage.class));
    }

    @Test
    @DisplayName("Expect RuntimeException When Update Stock Is Invalid")
    void Expect_RuntimeException_When_UpdateStockIsInvalid() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        Order order=TestUtilOrder.buildOrderMock2();
        assertThrows(RuntimeException.class, () -> orderBusAdapter.updateStock(order));

        verify(objectMapper, times(1)).writeValueAsString(any());
        verify(sender, never()).sendMessage(any(ServiceBusMessage.class));
    }
}
