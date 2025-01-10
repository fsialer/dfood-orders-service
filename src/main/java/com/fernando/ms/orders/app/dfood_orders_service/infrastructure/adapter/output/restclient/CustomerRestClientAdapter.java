package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalCustomersOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.CustomerFeignClient;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerRestClientAdapter implements ExternalCustomersOutputPort {
    private final CustomerFeignClient client;

    @Override
    public void verifyExistsById(Long id) {
        client.verifyExistsById(id);
    }
}
