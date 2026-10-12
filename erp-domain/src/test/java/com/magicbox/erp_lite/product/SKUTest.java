package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.SKU;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("SKU Domain Test")
class SKUTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when value is null or blank")
        void shouldThrowWhenValueIsNullOrBlank(String invalidValue) {
            assertThrows(IllegalArgumentException.class, () -> new SKU(invalidValue));
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(strings = {"laptop-001", "LAPTOP001", "LAPTOP-01", "LAPTOP-0001", "123-001", "LAPTOP-abc"})
        @DisplayName("Should throw IllegalArgumentException when value does not match the expected format")
        void shouldThrowWhenFormatIsInvalid(String invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new SKU(invalidValue));

            assertEquals("Invalid SKU format (expected e.g. LAPTOP-001): " + invalidValue, exception.getMessage());
        }

        @Test
        @DisplayName("Should create a SKU with a valid format")
        void shouldCreateSkuWithValidFormat() {
            SKU sku = new SKU("LAPTOP-001");

            assertEquals("LAPTOP-001", sku.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create a SKU using the static factory method")
        void shouldCreateSkuUsingFactory() {
            SKU sku = SKU.of("LAPTOP-002");

            assertEquals("LAPTOP-002", sku.value());
        }
    }
}
