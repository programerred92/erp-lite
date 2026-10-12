package com.magicbox.erp_lite.domain.shared;

import com.magicbox.erp_lite.domain.common.Guard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Immutable monetary value with currency.
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Guard.notNull(amount, "amount");
        Guard.notNull(currency, "currency");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        amount = amount.setScale(Math.max(currency.getDefaultFractionDigits(), 0), RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(double amount, Currency currency) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("amount must be a finite number");
        }
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must be >= 0");
        }
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    public Money multiply(Quantity quantity) {
        Guard.notNull(quantity, "quantity");
        return multiply(quantity.value());
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean hasSameCurrency(Money other) {
        Guard.notNull(other, "other");
        return currency.equals(other.currency);
    }

    private void requireSameCurrency(Money other) {
        if (!hasSameCurrency(other)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + currency + " vs " + other.currency);
        }
    }
}
