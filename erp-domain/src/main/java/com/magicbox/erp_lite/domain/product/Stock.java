package com.magicbox.erp_lite.domain.product;

/**
 * Product stock quantity. Cannot be negative.
 */
public record Stock(int value) {

    public Stock {
        if (value < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
    }

    public static Stock of(int value) {
        return new Stock(value);
    }

    public static Stock zero() {
        return new Stock(0);
    }

    public Stock increment(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity to increment must be > 0");
        }
        try {
            return new Stock(Math.addExact(value, quantity));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("stock overflow");
        }
    }

    public Stock decrement(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity to decrement must be > 0");
        }
        if (!hasAvailable(quantity)) {
            throw new IllegalArgumentException(
                    "Insufficient stock: available " + value + ", required " + quantity);
        }
        return new Stock(value - quantity);
    }

    public boolean hasAvailable(int required) {
        return value >= required;
    }
}
