package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    ORDER_NOT_FOUND("ORDER_MS_001", "Order not found."),
    ORDER_BAD_PARAMETERS("ORDER_MS_003", "Invalid parameters for creation order."),
    INTERNAL_SERVER_ERROR("ORDER_MS_004", "Internal server error.");

    private final String code;
    private final String message;
}
