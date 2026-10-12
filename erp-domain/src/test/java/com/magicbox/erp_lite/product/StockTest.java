package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Stock Domain Test")
class StockTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void shouldThrowWhenValueIsNegative() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Stock(-1));

            assertEquals("stock must be >= 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a Stock with zero value")
        void shouldCreateStockWithZeroValue() {
            Stock stock = new Stock(0);

            assertEquals(0, stock.value());
        }

        @Test
        @DisplayName("Should create a Stock with a positive value")
        void shouldCreateStockWithPositiveValue() {
            Stock stock = new Stock(10);

            assertEquals(10, stock.value());
        }
    }

    @Nested
    @DisplayName("of(value) and zero()")
    class Factories {

        @Test
        @DisplayName("Should create a Stock using the static factory method")
        void shouldCreateStockUsingFactory() {
            assertEquals(5, Stock.of(5).value());
        }

        @Test
        @DisplayName("Should create a Stock with zero value using zero()")
        void shouldCreateZeroStock() {
            assertEquals(0, Stock.zero().value());
        }
    }

    @Nested
    @DisplayName("increment(quantity)")
    class Increment {

        @ParameterizedTest(name = "quantity=[{0}] should throw IllegalArgumentException")
        @ValueSource(ints = {0, -1})
        @DisplayName("Should throw IllegalArgumentException when quantity is zero or negative")
        void shouldThrowWhenQuantityIsZeroOrNegative(int invalidQuantity) {
            Stock stock = Stock.of(10);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> stock.increment(invalidQuantity));

            assertEquals("quantity to increment must be > 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should return a new Stock with the incremented value")
        void shouldIncrementStockValue() {
            Stock stock = Stock.of(10);

            Stock result = stock.increment(5);

            assertEquals(15, result.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the increment causes an overflow")
        void shouldThrowWhenIncrementOverflows() {
            Stock stock = Stock.of(Integer.MAX_VALUE);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> stock.increment(1));

            assertEquals("stock overflow", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("decrement(quantity)")
    class Decrement {

        @ParameterizedTest(name = "quantity=[{0}] should throw IllegalArgumentException")
        @ValueSource(ints = {0, -1})
        @DisplayName("Should throw IllegalArgumentException when quantity is zero or negative")
        void shouldThrowWhenQuantityIsZeroOrNegative(int invalidQuantity) {
            Stock stock = Stock.of(10);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> stock.decrement(invalidQuantity));

            assertEquals("quantity to decrement must be > 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when there is insufficient stock")
        void shouldThrowWhenInsufficientStock() {
            Stock stock = Stock.of(5);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> stock.decrement(10));

            assertEquals("Insufficient stock: available 5, required 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should return a new Stock with the decremented value")
        void shouldDecrementStockValue() {
            Stock stock = Stock.of(10);

            Stock result = stock.decrement(4);

            assertEquals(6, result.value());
        }

        @Test
        @DisplayName("Should allow decrementing the exact available quantity, resulting in zero")
        void shouldAllowDecrementingExactAvailableQuantity() {
            Stock stock = Stock.of(10);

            Stock result = stock.decrement(10);

            assertEquals(0, result.value());
        }
    }

    @Nested
    @DisplayName("hasAvailable(required)")
    class HasAvailable {

        @Test
        @DisplayName("Should return true when stock is greater than required")
        void shouldReturnTrueWhenStockIsGreaterThanRequired() {
            assertTrue(Stock.of(10).hasAvailable(5));
        }

        @Test
        @DisplayName("Should return true when stock equals required")
        void shouldReturnTrueWhenStockEqualsRequired() {
            assertTrue(Stock.of(10).hasAvailable(10));
        }

        @Test
        @DisplayName("Should return false when stock is less than required")
        void shouldReturnFalseWhenStockIsLessThanRequired() {
            assertFalse(Stock.of(5).hasAvailable(10));
        }
    }
}
