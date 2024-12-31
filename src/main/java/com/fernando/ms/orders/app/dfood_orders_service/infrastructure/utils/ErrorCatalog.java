package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    ORDER_NOT_FOUND("ORDER_MS_001", "Order not found."),
    ORDER_BAD_PARAMETERS("ORDER_MS_002", "Invalid parameters for creation order."),
    //PRODUCT_NOT_FOUND("ORDER_MS_003","Product Not Found"),
    WEB_CLIENT_ERROR("ORDER_MS_003", "Error with product-service."),
    ORDER_STRATEGY_ERROR("ORDER_MS_004", "Status type selected is invalid."),
    INTERNAL_SERVER_ERROR("ORDER_MS_000", "Internal server error."),
    STATUS_ORDER_RULE("ORDER_MS_005", "Status order rule not allowed.");

    private final String code;
    private final String message;
}
