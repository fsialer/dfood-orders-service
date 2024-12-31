package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderEntity;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.repository.OrderJpaRepository;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.ProductFeignClient;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.mapper.ProductRestClientMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilOrder;
import com.fernando.ms.orders.app.dfood_orders_service.utils.TestUtilProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Test
    @DisplayName("When Products Identifier Are Availability Expect A List Products Successfully")
    void When_ProductsIdentifierAreAvailability_Expect_AListProductsSuccessfully() throws Exception {

        Product product = TestUtilProduct.buildProductMock();
        ProductClientResponse productClientResponse=TestUtilProduct.buildProductResponseMock();
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

    @Test
    @DisplayName("When Adding Products To Order Expect Order To Be Updated")
    void When_AddingProductsToOrder_Expect_OrderToBeUpdated() {
        OrderEntity orderEntity = TestUtilOrder.buildOrderEntityMock5();
        Product product = TestUtilProduct.buildProductMock();
        when(orderJpaRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        doNothing().when(productFeignClient).verifyExistsByIds(anyList());
        productsRestClientAdapter.addProductsToOrder(1L, new ArrayList<>(List.of(product)));

        verify(orderJpaRepository, times(1)).findById(anyLong());
        verify(productFeignClient, times(1)).verifyExistsByIds(anyList());
        verify(orderJpaRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("When Order Not Found Expect OrderNotFoundException")
    void When_OrderNotFound_Expect_OrderNotFoundException() {
        when(orderJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> productsRestClientAdapter.addProductsToOrder(1L, List.of()));
        verify(orderJpaRepository, times(1)).findById(anyLong());
    }
}
