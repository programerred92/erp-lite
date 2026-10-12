package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.Guard;

public record ProductName(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 200;

    public ProductName {
        Guard.notNull(value, "productName");
        value = value.strip();
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "productName length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
