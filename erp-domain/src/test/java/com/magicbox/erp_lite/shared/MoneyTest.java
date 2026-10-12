package com.magicbox.erp_lite.shared;

import com.magicbox.erp_lite.domain.shared.Money;
import com.magicbox.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Money Domain Test")
class MoneyTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void shouldThrowWhenAmountIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(null, USD));

            assertEquals("amount must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currency is null")
        void shouldThrowWhenCurrencyIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(BigDecimal.TEN, null));

            assertEquals("currency must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is negative")
        void shouldThrowWhenAmountIsNegative() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(BigDecimal.valueOf(-1), USD));

            assertEquals("amount must be >= 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should scale the amount to the currency's default fraction digits")
        void shouldScaleAmountToCurrencyFractionDigits() {
            Money money = new Money(new BigDecimal("10.005"), USD);

            assertEquals(new BigDecimal("10.01"), money.amount());
        }

        @Test
        @DisplayName("Should create Money with zero amount")
        void shouldCreateMoneyWithZeroAmount() {
            Money money = new Money(BigDecimal.ZERO, USD);

            assertEquals(0, money.amount().compareTo(BigDecimal.ZERO));
        }
    }

    @Nested
    @DisplayName("of(amount, currency) factory methods")
    class OfFactory {

        @Test
        @DisplayName("Should create Money from a BigDecimal amount")
        void shouldCreateMoneyFromBigDecimal() {
            Money money = Money.of(new BigDecimal("15.50"), USD);

            assertEquals(new BigDecimal("15.50"), money.amount());
        }

        @Test
        @DisplayName("Should create Money from a double amount")
        void shouldCreateMoneyFromDouble() {
            Money money = Money.of(15.5, USD);

            assertEquals(new BigDecimal("15.50"), money.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when double amount is NaN")
        void shouldThrowWhenDoubleAmountIsNaN() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Money.of(Double.NaN, USD));

            assertEquals("amount must be a finite number", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when double amount is infinite")
        void shouldThrowWhenDoubleAmountIsInfinite() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Money.of(Double.POSITIVE_INFINITY, USD));

            assertEquals("amount must be a finite number", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("zero(currency)")
    class ZeroFactory {

        @Test
        @DisplayName("Should create a Money instance with zero amount")
        void shouldCreateZeroMoney() {
            Money money = Money.zero(USD);

            assertEquals(0, money.amount().compareTo(BigDecimal.ZERO));
            assertFalse(money.isPositive());
        }
    }

    @Nested
    @DisplayName("add(other)")
    class Add {

        @Test
        @DisplayName("Should add two Money values with the same currency")
        void shouldAddMoneyWithSameCurrency() {
            Money first = Money.of(new BigDecimal("10.00"), USD);
            Money second = Money.of(new BigDecimal("5.00"), USD);

            Money result = first.add(second);

            assertEquals(new BigDecimal("15.00"), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currencies differ")
        void shouldThrowWhenCurrenciesDiffer() {
            Money first = Money.of(new BigDecimal("10.00"), USD);
            Money second = Money.of(new BigDecimal("5.00"), EUR);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> first.add(second));

            assertEquals("Currency mismatch: " + USD + " vs " + EUR, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("subtract(other)")
    class Subtract {

        @Test
        @DisplayName("Should subtract two Money values with the same currency")
        void shouldSubtractMoneyWithSameCurrency() {
            Money first = Money.of(new BigDecimal("10.00"), USD);
            Money second = Money.of(new BigDecimal("4.00"), USD);

            Money result = first.subtract(second);

            assertEquals(new BigDecimal("6.00"), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currencies differ")
        void shouldThrowWhenCurrenciesDiffer() {
            Money first = Money.of(new BigDecimal("10.00"), USD);
            Money second = Money.of(new BigDecimal("4.00"), EUR);

            assertThrows(IllegalArgumentException.class, () -> first.subtract(second));
        }
    }

    @Nested
    @DisplayName("multiply(multiplier)")
    class MultiplyByInt {

        @Test
        @DisplayName("Should multiply the amount by a positive integer")
        void shouldMultiplyByPositiveInteger() {
            Money money = Money.of(new BigDecimal("10.00"), USD);

            Money result = money.multiply(3);

            assertEquals(new BigDecimal("30.00"), result.amount());
        }

        @Test
        @DisplayName("Should multiply the amount by zero resulting in zero")
        void shouldMultiplyByZero() {
            Money money = Money.of(new BigDecimal("10.00"), USD);

            Money result = money.multiply(0);

            assertEquals(0, result.amount().compareTo(BigDecimal.ZERO));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when multiplier is negative")
        void shouldThrowWhenMultiplierIsNegative() {
            Money money = Money.of(new BigDecimal("10.00"), USD);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> money.multiply(-1));

            assertEquals("multiplier must be >= 0", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("multiply(quantity)")
    class MultiplyByQuantity {

        @Test
        @DisplayName("Should multiply the amount by the quantity's value")
        void shouldMultiplyByQuantityValue() {
            Money money = Money.of(new BigDecimal("10.00"), USD);

            Money result = money.multiply(Quantity.of(4));

            assertEquals(new BigDecimal("40.00"), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity is null")
        void shouldThrowWhenQuantityIsNull() {
            Money money = Money.of(new BigDecimal("10.00"), USD);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> money.multiply((Quantity) null));

            assertEquals("quantity must not be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("isPositive()")
    class IsPositive {

        @Test
        @DisplayName("Should return true when amount is greater than zero")
        void shouldReturnTrueWhenAmountIsPositive() {
            Money money = Money.of(new BigDecimal("0.01"), USD);

            assertTrue(money.isPositive());
        }

        @Test
        @DisplayName("Should return false when amount is zero")
        void shouldReturnFalseWhenAmountIsZero() {
            Money money = Money.zero(USD);

            assertFalse(money.isPositive());
        }
    }

    @Nested
    @DisplayName("hasSameCurrency(other)")
    class HasSameCurrency {

        @Test
        @DisplayName("Should return true when currencies match")
        void shouldReturnTrueWhenCurrenciesMatch() {
            Money first = Money.zero(USD);
            Money second = Money.of(new BigDecimal("1.00"), USD);

            assertTrue(first.hasSameCurrency(second));
        }

        @Test
        @DisplayName("Should return false when currencies differ")
        void shouldReturnFalseWhenCurrenciesDiffer() {
            Money first = Money.zero(USD);
            Money second = Money.zero(EUR);

            assertFalse(first.hasSameCurrency(second));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when other is null")
        void shouldThrowWhenOtherIsNull() {
            Money money = Money.zero(USD);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> money.hasSameCurrency(null));

            assertEquals("other must not be null", exception.getMessage());
        }
    }
}
