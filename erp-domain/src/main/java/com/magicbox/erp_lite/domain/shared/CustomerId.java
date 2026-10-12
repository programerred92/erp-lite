package com.magicbox.erp_lite.domain.shared;

import com.magicbox.erp_lite.domain.common.Guard;

/**
 * Reference to an external customer system (JSONPlaceholder).
 */
public record CustomerId(Long value) {

    public CustomerId {
        Guard.notNull(value, "customerId");
        if (value <= 0) {
            throw new IllegalArgumentException("customerId must be > 0");
        }
    }

    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
