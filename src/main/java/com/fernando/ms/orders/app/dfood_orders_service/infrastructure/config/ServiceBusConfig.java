package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ServiceBusConfig {
    @Value("${servicebus.connection-string}")
    private String connectionString;

    @Value("${servicebus.queue-name}")
    private String queueName;

//    @Bean
//    public ServiceBusSenderClient serviceBusSenderClient() {
//        return new ServiceBusClientBuilder()
//                .connectionString(connectionString)
//                .sender()
//                .queueName(queueName)
//                .buildClient();
//    }
}
