package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.OrderNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Year;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OrderNumber Domain Test")
class OrderNumberTest {

    private static final Pattern PATTERN = Pattern.compile("^ORD-\\d{4}-\\d{3}$");

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when value is null or blank")
        void shouldThrowWhenValueIsNullOrBlank(String invalidValue) {
            assertThrows(IllegalArgumentException.class, () -> new OrderNumber(invalidValue));
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(strings = {"ORD-2025-1", "ORD-25-001", "2025-001", "ORD-2025-0001", "ord-2025-001"})
        @DisplayName("Should throw IllegalArgumentException when value does not match the expected format")
        void shouldThrowWhenFormatIsInvalid(String invalidValue) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderNumber(invalidValue));

            assertEquals("Invalid order number format (expected ORD-YYYY-NNN): " + invalidValue,
                    exception.getMessage());
        }

        @Test
        @DisplayName("Should create an OrderNumber with a valid format")
        void shouldCreateOrderNumberWithValidFormat() {
            OrderNumber orderNumber = new OrderNumber("ORD-2025-001");

            assertEquals("ORD-2025-001", orderNumber.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create an OrderNumber using the static factory method")
        void shouldCreateOrderNumberUsingFactory() {
            OrderNumber orderNumber = OrderNumber.of("ORD-2025-002");

            assertEquals("ORD-2025-002", orderNumber.value());
        }
    }

    @Nested
    @DisplayName("generate()")
    class Generate {

        @Test
        @DisplayName("Should generate an OrderNumber matching the expected pattern for the current year")
        void shouldGenerateOrderNumberMatchingPattern() {
            OrderNumber orderNumber = OrderNumber.generate();

            assertTrue(PATTERN.matcher(orderNumber.value()).matches());
            assertTrue(orderNumber.value().startsWith("ORD-" + Year.now().getValue() + "-"));
        }
    }
}
