package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Guard;
import com.magicbox.erp_lite.domain.shared.CustomerId;

/**
 * Customer reference with basic info.
 */
public record Customer(CustomerId customerId, String customerName) {

    public Customer {
        Guard.notNull(customerId, "customerId");
        Guard.notBlank(customerName, "customerName");
    }

    public static Customer of(CustomerId customerId, String customerName) {
        return new Customer(customerId, customerName);
    }
}
