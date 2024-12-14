package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.OrderRestAdapter;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper.OrderRestMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.OrderPersistenceAdapter;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.ProductFeignClient;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.mapper.ProductRestClientMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductRestClientAdapterTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private ProductRestClientMapper productRestClientMapper;


    @InjectMocks
    private ProductsRestClientAdapter productsRestClientAdapter;

    @Test
    @DisplayName("When Products Identifier Are Availability Expect A List Products Successfully")
    void When_ProductsIdentifierAreAvailability_Expect_AListProductsSuccessfully() throws Exception {

        Product product = TestUtilProduct.buildProductMock();
        ProductClientResponse productClientResponse=TestUtilProduct.buildProductResponseMock();
        //List<OrderResponse> orderResponses= Collections.singletonList(TestUtilOrder.buildOrderResponseMock());

        when(productFeignClient.findByIds(anyList()))
                .thenReturn(Collections.singletonList(productClientResponse));

        when(productRestClientMapper.toProducts(anyList()))
                .thenReturn(Collections.singletonList(product));

        List<Product> products=productsRestClientAdapter.findAllProductsByIds(List.of(1L));

        assertNotNull(products);
        assertEquals(1,products.size());

        Mockito.verify(productFeignClient,times(1)).findByIds(anyList());
        Mockito.verify(productRestClientMapper,times(1)).toProducts(anyList());
    }

    @Test
    @DisplayName("When Products Identifier Are Availability Expect None Exception")
    void When_ProductsIdentifierAreAvailability_Expect_NoneException() throws Exception {
        doNothing().when(productFeignClient).verifyExistsByIds(anyList());
        assertDoesNotThrow(() -> productsRestClientAdapter.verifyExistProductsByIds(List.of(1L)));
        Mockito.verify(productFeignClient,times(1)).verifyExistsByIds(anyList());
    }
}
