package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.ExternalProductsInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderBusInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper.OrderRestMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestAdapter.class)
public class OrderRestAdapterTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderInputPort orderInputPort;

    @MockBean
    private OrderRestMapper orderRestMapper;

    @MockBean
    private ExternalProductsInputPort externalProductsInputPort;

    @MockBean
    private OrderBusInputPort orderBusInputPort;


    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper=new ObjectMapper();
    }

    @Test
    @DisplayName("When Orders Are Availability Expect Customers Information Successfully")
    void When_OrdersAreAvailability_Expect_OrdersInformationSuccessfully() throws Exception {

        Order order = TestUtilOrder.buildOrderMock();
        List<OrderResponse> orderResponses= Collections.singletonList(TestUtilOrder.buildOrderResponseMock());

        when(orderInputPort.findAll())
                .thenReturn(Collections.singletonList(order));

        when(orderRestMapper.toOrdersResponse(anyList()))
                .thenReturn(orderResponses);

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());

        Mockito.verify(orderInputPort,times(1)).findAll();
        Mockito.verify(orderRestMapper,times(1)).toOrdersResponse(anyList());
    }

    @Test
    @DisplayName("When_ Order Identifier Is Valid Expect Order Information Successfully")
    void When_OrderIdentifierIsValid_Expect_OrderInformationSuccessfully() throws Exception {

        Order order = TestUtilOrder.buildOrderMock();
        OrderResponse orderResponse= TestUtilOrder.buildOrderResponseMock();

        when(orderInputPort.findById(anyLong()))
                .thenReturn(order);

        when(orderRestMapper.toOrderResponse(any(Order.class)))
                .thenReturn(orderResponse);

        mockMvc.perform(get("/orders/{id}",1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andDo(print());

        Mockito.verify(orderInputPort,times(1)).findById(anyLong());
        Mockito.verify(orderRestMapper,times(1)).toOrderResponse(any(Order.class));
    }

    @Test
    @DisplayName("When Order Identifier Is Valid Expect Order Information Successfully")
    void When_OrderInformationIsCorrect_Expect_OrderInformationToBeSavedSuccessfully() throws Exception {

        CreateOrderRequest createOrderRequest = TestUtilOrder.buildCreateOrderRequestMock();
        OrderResponse orderResponse = TestUtilOrder.buildOrderResponseMock();

        when(orderRestMapper.toOrder(any(CreateOrderRequest.class))).thenReturn(TestUtilOrder.buildOrderMock());
        when(orderInputPort.save(any())).thenReturn(TestUtilOrder.buildOrderMock());
        when(orderRestMapper.toOrderResponse(any())).thenReturn(orderResponse);
        doNothing().when(externalProductsInputPort).addProductsToOrder(anyLong(), any());
        doNothing().when(orderBusInputPort).updateStock(any());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(orderInputPort, times(1)).save(any());
        //Mockito.verify(orderRestMapper, times(1)).toOrder(any(CreateOrderRequest.class));
        Mockito.verify(orderRestMapper, times(1)).toOrderResponse(any());
        Mockito.verify(externalProductsInputPort, times(1)).addProductsToOrder(anyLong(), any());
        Mockito.verify(orderBusInputPort, times(1)).updateStock(any());
        Mockito.verify(externalProductsInputPort,times(1)).addProductsToOrder(anyLong(),any());
        Mockito.verify(orderBusInputPort,times(1)).updateStock(any());
    }

    @Test
    @DisplayName("When Order Identifier Is Valid Expect Order Information To Be Updated Successfully")
    void When_ChangeStatusOrderInformationIsCorrect_Expect_OrderInformationToBeUpdatedSuccessfully() throws Exception {
        Order order = TestUtilOrder.buildOrderMock();
        OrderResponse orderResponse= TestUtilOrder.buildOrderResponseMock();
        when(orderInputPort.changeStatus(anyLong(),anyString()))
                .thenReturn(order);
        when(orderRestMapper.toOrderResponse(any(Order.class))).thenReturn(orderResponse);

        mockMvc.perform(put("/orders/{id}/change-status/{status}",1L,"CANCELED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andDo(print());

        Mockito.verify(orderInputPort,times(1)).changeStatus(anyLong(),anyString());
    }



}
