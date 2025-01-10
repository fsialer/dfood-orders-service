package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "customers-service",url = "${customers-service.url}")
public interface CustomerFeignClient {
    @GetMapping("/verify-exists-by-id")
    void verifyExistsById(@RequestParam Long id);
}
