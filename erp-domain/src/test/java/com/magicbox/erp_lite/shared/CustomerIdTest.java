package com.magicbox.erp_lite.shared;

import com.magicbox.erp_lite.domain.shared.CustomerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CustomerId Domain Test")
class CustomerIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerId(null));

            assertEquals("customerId must not be null", exception.getMessage());
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(longs = {0L, -1L, -100L})
        @DisplayName("Should throw IllegalArgumentException when value is zero or negative")
        void shouldThrowWhenValueIsZeroOrNegative(long invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerId(invalidValue));

            assertEquals("customerId must be > 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a CustomerId with a positive value")
        void shouldCreateCustomerIdWithPositiveValue() {
            CustomerId customerId = new CustomerId(42L);

            assertEquals(42L, customerId.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create a CustomerId using the static factory method")
        void shouldCreateCustomerIdUsingFactory() {
            CustomerId customerId = CustomerId.of(7L);

            assertEquals(7L, customerId.value());
        }
    }
}
