package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.OrderId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("OrderId Domain Test")
class OrderIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderId(null));

            assertEquals("orderId must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create an OrderId with the provided UUID")
        void shouldCreateOrderIdWithProvidedUuid() {
            UUID uuid = UUID.randomUUID();

            OrderId orderId = new OrderId(uuid);

            assertEquals(uuid, orderId.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create an OrderId using the static factory method")
        void shouldCreateOrderIdUsingFactory() {
            UUID uuid = UUID.randomUUID();

            OrderId orderId = OrderId.of(uuid);

            assertEquals(uuid, orderId.value());
        }
    }

    @Nested
    @DisplayName("generate()")
    class Generate {

        @Test
        @DisplayName("Should generate distinct OrderId instances on each call")
        void shouldGenerateDistinctOrderIds() {
            OrderId first = OrderId.generate();
            OrderId second = OrderId.generate();

            assertNotEquals(first, second);
        }
    }
}
