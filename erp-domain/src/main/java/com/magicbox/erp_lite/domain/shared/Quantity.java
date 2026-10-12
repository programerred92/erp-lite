package com.magicbox.erp_lite.domain.shared;

/**
 * Quantity of items in an order. Must be greater than 0.
 */
public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
