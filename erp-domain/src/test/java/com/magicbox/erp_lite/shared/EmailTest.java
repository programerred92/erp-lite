package com.magicbox.erp_lite.shared;

import com.magicbox.erp_lite.domain.shared.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Email Domain Test")
class EmailTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should throw IllegalArgumentException when value is null or blank")
        void shouldThrowWhenValueIsNullOrBlank(String invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Email(invalidValue));

            assertEquals("email must not be " + (invalidValue == null ? "null" : "blank"), exception.getMessage());
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(strings = {"invalid", "invalid@", "@domain.com", "invalid@domain", "invalid domain@test.com"})
        @DisplayName("Should throw IllegalArgumentException when value has an invalid format")
        void shouldThrowWhenFormatIsInvalid(String invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Email(invalidValue));

            assertEquals("Invalid email format: " + invalidValue, exception.getMessage());
        }

        @Test
        @DisplayName("Should create an Email with a valid format")
        void shouldCreateEmailWithValidFormat() {
            Email email = new Email("user@example.com");

            assertEquals("user@example.com", email.value());
        }

        @Test
        @DisplayName("Should strip surrounding whitespace before validating")
        void shouldStripSurroundingWhitespace() {
            Email email = new Email("  user@example.com  ");

            assertEquals("user@example.com", email.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create an Email using the static factory method")
        void shouldCreateEmailUsingFactory() {
            Email email = Email.of("user@example.com");

            assertEquals("user@example.com", email.value());
        }
    }
}
