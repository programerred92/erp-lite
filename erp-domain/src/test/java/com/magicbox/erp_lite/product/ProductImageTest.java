package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ProductImage Domain Test")
class ProductImageTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when imageUrl is null or blank")
        void shouldThrowWhenImageUrlIsNullOrBlank(String invalidValue) {
            assertThrows(IllegalArgumentException.class, () -> new ProductImage(invalidValue));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the URL does not use http or https scheme")
        void shouldThrowWhenSchemeIsNotHttp() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductImage("ftp://bucket.s3.amazonaws.com/image.png"));

            assertEquals("Invalid image URL: ftp://bucket.s3.amazonaws.com/image.png", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the URL has no host")
        void shouldThrowWhenHostIsMissing() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductImage("https:///image.png"));

            assertEquals("Invalid image URL: https:///image.png", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the URL is syntactically invalid")
        void shouldThrowWhenUrlIsSyntacticallyInvalid() {
            assertThrows(IllegalArgumentException.class, () -> new ProductImage("http://^invalid^"));
        }

        @Test
        @DisplayName("Should create a ProductImage with a valid https URL")
        void shouldCreateProductImageWithValidHttpsUrl() {
            ProductImage image = new ProductImage("https://bucket.s3.amazonaws.com/image.png");

            assertEquals("https://bucket.s3.amazonaws.com/image.png", image.imageUrl());
        }

        @Test
        @DisplayName("Should create a ProductImage with a valid http URL")
        void shouldCreateProductImageWithValidHttpUrl() {
            ProductImage image = new ProductImage("http://bucket.s3.amazonaws.com/image.png");

            assertEquals("http://bucket.s3.amazonaws.com/image.png", image.imageUrl());
        }

        @Test
        @DisplayName("Should strip surrounding whitespace before validating")
        void shouldStripSurroundingWhitespace() {
            ProductImage image = new ProductImage("  https://bucket.s3.amazonaws.com/image.png  ");

            assertEquals("https://bucket.s3.amazonaws.com/image.png", image.imageUrl());
        }
    }

    @Nested
    @DisplayName("of(imageUrl)")
    class OfFactory {

        @Test
        @DisplayName("Should create a ProductImage using the static factory method")
        void shouldCreateProductImageUsingFactory() {
            ProductImage image = ProductImage.of("https://bucket.s3.amazonaws.com/image.png");

            assertEquals("https://bucket.s3.amazonaws.com/image.png", image.imageUrl());
        }
    }

    @Nested
    @DisplayName("getFullUrl()")
    class GetFullUrl {

        @Test
        @DisplayName("Should return the stored image URL")
        void shouldReturnStoredImageUrl() {
            ProductImage image = ProductImage.of("https://bucket.s3.amazonaws.com/image.png");

            assertEquals("https://bucket.s3.amazonaws.com/image.png", image.getFullUrl());
        }
    }

    @Nested
    @DisplayName("getFileName()")
    class GetFileName {

        @Test
        @DisplayName("Should return the file name extracted from the URL path")
        void shouldReturnFileNameFromPath() {
            ProductImage image = ProductImage.of("https://bucket.s3.amazonaws.com/products/image.png");

            assertEquals("image.png", image.getFileName());
        }

        @Test
        @DisplayName("Should return an empty string when the URL has no path")
        void shouldReturnEmptyStringWhenNoPath() {
            ProductImage image = ProductImage.of("https://bucket.s3.amazonaws.com");

            assertEquals("", image.getFileName());
        }
    }
}
