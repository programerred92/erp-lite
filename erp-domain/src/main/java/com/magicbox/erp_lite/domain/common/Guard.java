package com.magicbox.erp_lite.domain.common;

/**
 * Validation helpers that throw {@link IllegalArgumentException} with clear messages.
 */
public final class Guard {

    private Guard() {
    }

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        return value;
    }

    public static String notBlank(String value, String name) {
        notNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
