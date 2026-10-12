package com.magicbox.erp_lite.shared;

import com.magicbox.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Quantity Domain Test")
class QuantityTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(ints = {0, -1, -100})
        @DisplayName("Should throw IllegalArgumentException when value is zero or negative")
        void shouldThrowWhenValueIsZeroOrNegative(int invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Quantity(invalidValue));

            assertEquals("quantity must be > 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a Quantity with a positive value")
        void shouldCreateQuantityWithPositiveValue() {
            Quantity quantity = new Quantity(5);

            assertEquals(5, quantity.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create a Quantity using the static factory method")
        void shouldCreateQuantityUsingFactory() {
            Quantity quantity = Quantity.of(10);

            assertEquals(10, quantity.value());
        }
    }

    @Nested
    @DisplayName("equals and hashCode contracts")
    class ObjectContracts {

        @Test
        @DisplayName("Should be equal when values are the same")
        void shouldBeEqualWhenValuesAreTheSame() {
            assertEquals(Quantity.of(3), Quantity.of(3));
            assertEquals(Quantity.of(3).hashCode(), Quantity.of(3).hashCode());
        }
    }
}
