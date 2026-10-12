package com.magicbox.erp_lite.catalog;

import com.magicbox.erp_lite.domain.catalog.CatalogType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CatalogType Domain Test")
class CatalogTypeTest {

    @Nested
    @DisplayName("fromCode(code)")
    class FromCode {

        @ParameterizedTest(name = "code=[{0}] should resolve to the matching enum value")
        @EnumSource(CatalogType.class)
        @DisplayName("Should resolve every valid code to its corresponding enum value")
        void shouldResolveValidCode(CatalogType type) {
            assertEquals(type, CatalogType.fromCode(type.getCode()));
        }

        @ParameterizedTest(name = "code=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when code is null or blank")
        void shouldThrowWhenCodeIsNullOrBlank(String invalidCode) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> CatalogType.fromCode(invalidCode));

            assertEquals("Catalog type code cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when code does not match any enum value")
        void shouldThrowWhenCodeIsInvalid() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> CatalogType.fromCode("UNKNOWN_CODE"));

            assertTrue(exception.getMessage().contains("Invalid catalog type code: 'UNKNOWN_CODE'"));
        }
    }

    @Nested
    @DisplayName("isValid(code)")
    class IsValid {

        @Test
        @DisplayName("Should return true for a valid code")
        void shouldReturnTrueForValidCode() {
            assertTrue(CatalogType.isValid("PRODUCT_CATEGORIES"));
        }

        @Test
        @DisplayName("Should return false for an invalid code")
        void shouldReturnFalseForInvalidCode() {
            assertFalse(CatalogType.isValid("UNKNOWN_CODE"));
        }

        @Test
        @DisplayName("Should return false for a null code")
        void shouldReturnFalseForNullCode() {
            assertFalse(CatalogType.isValid(null));
        }
    }

    @Nested
    @DisplayName("getDisplayName()")
    class GetDisplayName {

        @Test
        @DisplayName("Should return the human readable display name")
        void shouldReturnDisplayName() {
            assertEquals("Product Categories", CatalogType.PRODUCT_CATEGORIES.getDisplayName());
        }
    }
}
