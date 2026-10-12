package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.regex.Pattern;

/**
 * Stock Keeping Unit. Unique and immutable. Pattern: LAPTOP-001
 */
public record SKU(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z]+-\\d{3}$");

    public SKU {
        Guard.notBlank(value, "sku");
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid SKU format (expected e.g. LAPTOP-001): " + value);
        }
    }

    public static SKU of(String value) {
        return new SKU(value);
    }
}
