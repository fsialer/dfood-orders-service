package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.CustomerFeignClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CustomerRestClientAdapterTest {

    @Mock
    private CustomerFeignClient client;

    @InjectMocks
    private CustomerRestClientAdapter customerRestClientAdapter;

    @Test
    @DisplayName("When Dealer Identifier Is Correct Expect Response204")
    void When_DealerIdentifierIsCorrect_Expect_Response204(){
        doNothing().when(client).verifyExistsById(anyLong());
        assertDoesNotThrow(() -> customerRestClientAdapter.verifyExistsById(1L));
        Mockito.verify(client,times(1)).verifyExistsById(anyLong());
    }

}
