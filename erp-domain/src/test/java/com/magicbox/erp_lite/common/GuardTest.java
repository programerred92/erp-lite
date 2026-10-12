package com.magicbox.erp_lite.common;

import com.magicbox.erp_lite.domain.common.Guard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Guard Domain Test")
class GuardTest {

    @Nested
    @DisplayName("notNull(value, name)")
    class NotNull {

        @Test
        @DisplayName("Should throw IllegalArgumentException with a clear message when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Guard.notNull(null, "field"));

            assertEquals("field must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should return the same value when it is not null")
        void shouldReturnValueWhenNotNull() {
            String value = "some-value";

            String result = Guard.notNull(value, "field");

            assertEquals(value, result);
        }
    }

    @Nested
    @DisplayName("notBlank(value, name)")
    class NotBlank {

        @Test
        @DisplayName("Should throw IllegalArgumentException with a clear message when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Guard.notBlank(null, "field"));

            assertEquals("field must not be null", exception.getMessage());
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
        @DisplayName("Should throw IllegalArgumentException with a clear message when value is blank")
        void shouldThrowWhenValueIsBlank(String blankValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Guard.notBlank(blankValue, "field"));

            assertEquals("field must not be blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should return the same value when it is not blank")
        void shouldReturnValueWhenNotBlank() {
            String value = "some-value";

            String result = Guard.notBlank(value, "field");

            assertEquals(value, result);
        }
    }
}
