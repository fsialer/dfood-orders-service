package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "products-service",url = "${products-service.url}")
public interface ProductFeignClient {
    @GetMapping("products/find-by-ids")
    List<ProductClientResponse> findByIds(@RequestParam List<Long> ids);
    @GetMapping("products/verify-exists-by-ids")
    void verifyExistsByIds(@RequestParam List<Long> ids);
}
