package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.restclient.models.response.ProductClientResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    List<Product> toProducts(List<ProductClientResponse> productsClient);
}
