package com.fernando.ms.orders.app.dfood_orders_service.application.ports.output;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;

import java.util.List;

public interface ExternalProductsOutputPort {
    void addProductsToOrder(Long orderId,List<Product> products);
    List<Product> findAllProductsByIds(List<Long> ids);
    void verifyExistProductsByIds(List<Long> ids);
}
