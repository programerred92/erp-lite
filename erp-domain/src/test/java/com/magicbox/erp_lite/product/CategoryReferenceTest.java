package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.product.CategoryReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CategoryReference Domain Test")
class CategoryReferenceTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "categoryId=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when categoryId is null or blank")
        void shouldThrowWhenCategoryIdIsNullOrBlank(String invalidValue) {
            assertThrows(IllegalArgumentException.class, () -> new CategoryReference(invalidValue));
        }

        @Test
        @DisplayName("Should create a CategoryReference with the provided categoryId")
        void shouldCreateCategoryReferenceWithProvidedId() {
            CategoryReference reference = new CategoryReference("cat-electronics");

            assertEquals("cat-electronics", reference.categoryId());
        }
    }

    @Nested
    @DisplayName("of(categoryId)")
    class OfFactory {

        @Test
        @DisplayName("Should create a CategoryReference using the static factory method")
        void shouldCreateCategoryReferenceUsingFactory() {
            CategoryReference reference = CategoryReference.of("cat-electronics");

            assertEquals("cat-electronics", reference.categoryId());
        }
    }
}
