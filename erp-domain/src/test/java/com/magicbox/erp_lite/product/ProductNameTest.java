package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ProductName Domain Test")
class ProductNameTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @NullSource
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull(String invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductName(invalidValue));

            assertEquals("productName must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is shorter than the minimum length")
        void shouldThrowWhenValueIsTooShort() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductName("ab"));

            assertEquals("productName length must be between 3 and 200", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is longer than the maximum length")
        void shouldThrowWhenValueIsTooLong() {
            String tooLong = "a".repeat(201);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductName(tooLong));

            assertEquals("productName length must be between 3 and 200", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a ProductName with a value at the minimum allowed length")
        void shouldCreateProductNameAtMinimumLength() {
            ProductName productName = new ProductName("abc");

            assertEquals("abc", productName.value());
        }

        @Test
        @DisplayName("Should create a ProductName with a value at the maximum allowed length")
        void shouldCreateProductNameAtMaximumLength() {
            String maxLength = "a".repeat(200);

            ProductName productName = new ProductName(maxLength);

            assertEquals(maxLength, productName.value());
        }

        @Test
        @DisplayName("Should strip surrounding whitespace before validating the length")
        void shouldStripSurroundingWhitespace() {
            ProductName productName = new ProductName("  Laptop  ");

            assertEquals("Laptop", productName.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create a ProductName using the static factory method")
        void shouldCreateProductNameUsingFactory() {
            ProductName productName = ProductName.of("Laptop");

            assertEquals("Laptop", productName.value());
        }
    }
}
