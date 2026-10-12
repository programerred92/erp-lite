package com.magicbox.erp_lite.domain.shared;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.regex.Pattern;

/**
 * Email address for notifications.
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        Guard.notBlank(value, "email");
        value = value.strip();
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
