package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.bus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.orders.app.dfood_orders_service.application.ports.output.OrderBusOutputPort;
import com.fernando.ms.orders.app.dfood_orders_service.domain.models.Order;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.output.bus.models.request.ProductStock;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBusAdapter implements OrderBusOutputPort {
    private final ServiceBusSenderClient sender;
    private final ObjectMapper objectMapper;
    public OrderBusAdapter(ServiceBusSenderClient sender, ObjectMapper objectMapper) {
        this.sender = sender;
        this.objectMapper = objectMapper;
    }

    @Override
    public void updateStock(Order order) {

        try {
            List<ProductStock> list=order.getProducts().stream().map(p->new ProductStock(p.getId(),p.getQuantity())).toList();
            String messageContent = this.objectMapper.writeValueAsString(list);
            sender.sendMessage(new ServiceBusMessage(messageContent));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
