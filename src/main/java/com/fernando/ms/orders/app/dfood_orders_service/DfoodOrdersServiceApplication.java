package com.fernando.ms.orders.app.dfood_orders_service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@RequiredArgsConstructor
public class DfoodOrdersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfoodOrdersServiceApplication.class, args);
	}

}
