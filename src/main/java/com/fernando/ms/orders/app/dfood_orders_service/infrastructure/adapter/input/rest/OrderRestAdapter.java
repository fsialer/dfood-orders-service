package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest;

import com.fernando.ms.orders.app.dfood_orders_service.application.ports.input.OrderInputPort;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.mapper.OrderRestMapper;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request.CreateOrderRequest;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderRestAdapter {
    private final OrderInputPort orderInputPort;
    private final OrderRestMapper orderRestMapper;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(){
        return ResponseEntity.ok().body(orderRestMapper.toOrdersResponse(orderInputPort.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(orderRestMapper.toOrderResponse(orderInputPort.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> save(@Valid @RequestBody CreateOrderRequest rq){
        OrderResponse orderResponse=orderRestMapper.toOrderResponse(orderInputPort.save(orderRestMapper.toOrder(rq)));
        return ResponseEntity.created(URI.create("/order/".concat(orderResponse.getId().toString()))).body(orderResponse);
    }

    @PutMapping("/{id}/change-status/{status}")
    public ResponseEntity<OrderResponse> changeStatus(@PathVariable(name = "id") Long id,@PathVariable(name = "status") String status){
        OrderResponse orderResponse=orderRestMapper.toOrderResponse(orderInputPort.changeStatus(id,status));
        return ResponseEntity.ok().body(orderResponse);
    }
}
