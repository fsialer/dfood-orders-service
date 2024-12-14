package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper.OrderRestMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.utils.ErrorCatalog.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {OrderRestAdapter.class})
public class GlobalControllerAdviceTest {
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
    @DisplayName("Expect OrderNotFoundException When Order Identifier Is Unknown")
    void Expect_OrderNotFoundException_When_OrderIdentifierIsUnknown() throws Exception {
        when(orderInputPort.findById(anyLong()))
                .thenThrow(new OrderNotFoundException());
        mockMvc.perform(get("/orders/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class);
                    assertAll(
                            ()->assertEquals(ORDER_NOT_FOUND.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(ORDER_NOT_FOUND.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });

    }

    @Test
    @DisplayName("Expect MethodArgumentNotValidException When Order Information Is Invalid")
    void Expect_MethodArgumentNotValidException_When_OrderInformationIsInvalid() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );
                    assertAll(
                            ()->assertEquals(ORDER_BAD_PARAMETERS.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(ORDER_BAD_PARAMETERS.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getDetails()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    @DisplayName("Expect FeignException When Products Of Order Are Invalid")
    void Expect_FeignException_When_ProductsOfOrderAreInvalid() throws Exception {
        CreateOrderRequest createOrderRequest= TestUtilOrder.buildCreateOrderRequestMock();
        Order order = TestUtilOrder.buildOrderMock();

        when(orderRestMapper.toOrder(any(CreateOrderRequest.class)))
                .thenReturn(order);

        FeignException feignException = FeignException.errorStatus(
                "POST /products",
                feign.Response.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .reason("Product not found")
                        .request(feign.Request.create(feign.Request.HttpMethod.POST, "/products", Collections.emptyMap(), null, null, null))
                        .build()
        );

        doThrow(feignException).when(orderInputPort).save(any(Order.class));
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(result -> {
                    // Parsear la respuesta para verificar los datos
                    ErrorResponse errorResponse = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            ErrorResponse.class
                    );

                    // Assert: Validar que la respuesta sea como se espera
                    assertAll(
                            () -> assertEquals(WEB_CLIENT_ERROR.getCode(), errorResponse.getCode()),
                            () -> assertEquals(FUNCTIONAL, errorResponse.getType()),
                            () -> assertEquals(WEB_CLIENT_ERROR.getMessage(), errorResponse.getMessage()),
                            () -> assertNotNull(errorResponse.getDetails()),
                            () -> assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    void Expect_RuntimeException_When_OrderInformationIsInvalid() throws Exception {
        when(orderInputPort.findAll())
                .thenThrow(new RuntimeException("Generic error"));
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );

                    assertAll(
                            ()->assertEquals(INTERNAL_SERVER_ERROR.getCode(),errorResponse.getCode()),
                            ()->assertEquals(SYSTEM,errorResponse.getType()),
                            ()->assertEquals(INTERNAL_SERVER_ERROR.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getDetails()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }


}
