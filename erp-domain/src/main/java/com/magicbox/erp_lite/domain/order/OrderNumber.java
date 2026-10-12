package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Guard;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * Unique order number. Pattern: ORD-2025-001. Uniqueness is enforced outside the domain.
 */
public record OrderNumber(String value) {

    private static final Pattern PATTERN = Pattern.compile("^ORD-\\d{4}-\\d{3}$");

    public OrderNumber {
        Guard.notBlank(value, "orderNumber");
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid order number format (expected ORD-YYYY-NNN): " + value);
        }
    }

    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    public static OrderNumber generate() {
        int sequence = ThreadLocalRandom.current().nextInt(1, 1000);
        return new OrderNumber("ORD-%d-%03d".formatted(Year.now().getValue(), sequence));
    }
}
