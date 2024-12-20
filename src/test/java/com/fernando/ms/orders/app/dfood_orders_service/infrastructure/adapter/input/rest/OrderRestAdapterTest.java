package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
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

        CreateOrderRequest createOrderRequest=TestUtilOrder.buildCreateOrderRequestMock();

        Order order = TestUtilOrder.buildOrderMock();
        OrderResponse orderResponse= TestUtilOrder.buildOrderResponseMock();

        when(orderInputPort.save(any(Order.class)))
                .thenReturn(order);

        when(orderRestMapper.toOrder(any(CreateOrderRequest.class)))
                .thenReturn(order);
        when(orderRestMapper.toOrderResponse(any(Order.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andDo(print());

        Mockito.verify(orderInputPort,times(1)).save(any(Order.class));
        Mockito.verify(orderRestMapper,times(1)).toOrder(any(CreateOrderRequest.class));
        Mockito.verify(orderRestMapper,times(1)).toOrderResponse(any(Order.class));
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
