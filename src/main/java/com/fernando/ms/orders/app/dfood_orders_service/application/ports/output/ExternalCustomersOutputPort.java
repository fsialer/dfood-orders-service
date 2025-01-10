package com.fernando.ms.orders.app.dfood_orders_service.application.ports.output;

import java.util.List;

public interface ExternalCustomersOutputPort {
    void verifyExistsById(Long id);
}
