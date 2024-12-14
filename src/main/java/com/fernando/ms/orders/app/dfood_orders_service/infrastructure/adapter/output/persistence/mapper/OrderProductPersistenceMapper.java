package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.orders.app.dfood_orders_service.domain.models.OrderProduct;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.persistence.models.OrderProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderProductPersistenceMapper {
    List<OrderProduct> toOrderProducts(List<OrderProductEntity> orderProducts);
    List<OrderProductEntity> toOrderProductsEntity(List<OrderProduct> orderProducts);
    OrderProductEntity toOrderProductEntity(OrderProduct orderProduct);
}
