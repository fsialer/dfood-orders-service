package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotNull
    private Long userId;
    //@NotNull
    //private LocalDate dateOrder;
    @NotNull
    private Double totalAmount;
//    @NotBlank
//    private StatusOrderEnum statusOrder;
    @NotEmpty
    private List<CreateOrderProduct> products;
}
