package com.fernando.ms.orders.app.dfood_orders_service.application.services;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.ExternalProductsOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderPersistencePort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.ProductNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderInputPort {

    private final OrderPersistencePort orderPersistencePort;
    private final ExternalProductsOutputPort externalProductsOutputPort;

    @Override
    public List<Order> findAll() {
        return orderPersistencePort.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderPersistencePort.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(Order order) {
        List<Long> productsIds=order.getProducts()
                .stream()
                .map(Product::getId)
                .toList();
        //List<Product> prods=externalProductsOutputPort.findAllProductsByIds(productsIds);
        externalProductsOutputPort.verifyExistProductsByIds(productsIds);

//        if(!new HashSet<>(prods.stream().map(Product::getId).toList()).containsAll(productsIds)){
//            throw  new ProductNotFoundException();
//        }
        return orderPersistencePort.save(order);
    }

}
