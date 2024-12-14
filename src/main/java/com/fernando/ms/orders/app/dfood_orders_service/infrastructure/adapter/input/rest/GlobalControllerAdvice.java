package com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest;

import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.OrderNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.domain.exception.ProductNotFoundException;
import com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Collections;

import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.orders.app.dfood_orders_service.infrastructure.utils.ErrorCatalog.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    private final String ERROR_LOG_MESSAGE = "Error -> code:{}, message: {}";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleOrderNotFoundException() {

        return ErrorResponse.builder()
                .code(ORDER_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(ORDER_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        return ErrorResponse.builder()
                .code(ORDER_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(ORDER_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build();
    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ErrorResponse handleProductNotFoundException() {
//        return ErrorResponse.builder()
//                .code(PRODUCT_NOT_FOUND.getCode())
//                .type(FUNCTIONAL)
//                .message(PRODUCT_NOT_FOUND.getMessage())
//                .timestamp(LocalDate.now().toString())
//                .build();
//    }

    @ExceptionHandler(FeignException.class)
    public  ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        ErrorResponse errorResponse =null;

        errorResponse= ErrorResponse.builder()
                .code(WEB_CLIENT_ERROR.getCode())
                .type(FUNCTIONAL)
                .message(WEB_CLIENT_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
        return ResponseEntity.status(e.status())
                .body(errorResponse);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }

}
