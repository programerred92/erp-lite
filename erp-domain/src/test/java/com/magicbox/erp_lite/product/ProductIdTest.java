package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ProductId Domain Test")
class ProductIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductId(null));

            assertEquals("productId must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a ProductId with the provided UUID")
        void shouldCreateProductIdWithProvidedUuid() {
            UUID uuid = UUID.randomUUID();

            ProductId productId = new ProductId(uuid);

            assertEquals(uuid, productId.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create a ProductId using the static factory method")
        void shouldCreateProductIdUsingFactory() {
            UUID uuid = UUID.randomUUID();

            ProductId productId = ProductId.of(uuid);

            assertEquals(uuid, productId.value());
        }
    }

    @Nested
    @DisplayName("generate()")
    class Generate {

        @Test
        @DisplayName("Should generate distinct ProductId instances on each call")
        void shouldGenerateDistinctProductIds() {
            ProductId first = ProductId.generate();
            ProductId second = ProductId.generate();

            assertNotEquals(first, second);
        }
    }
}
