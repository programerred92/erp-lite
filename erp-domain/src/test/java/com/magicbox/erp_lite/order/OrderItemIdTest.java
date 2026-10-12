package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.OrderItemId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("OrderItemId Domain Test")
class OrderItemIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderItemId(null));

            assertEquals("orderItemId must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create an OrderItemId with the provided UUID")
        void shouldCreateOrderItemIdWithProvidedUuid() {
            UUID uuid = UUID.randomUUID();

            OrderItemId orderItemId = new OrderItemId(uuid);

            assertEquals(uuid, orderItemId.value());
        }
    }

    @Nested
    @DisplayName("of(value)")
    class OfFactory {

        @Test
        @DisplayName("Should create an OrderItemId using the static factory method")
        void shouldCreateOrderItemIdUsingFactory() {
            UUID uuid = UUID.randomUUID();

            OrderItemId orderItemId = OrderItemId.of(uuid);

            assertEquals(uuid, orderItemId.value());
        }
    }

    @Nested
    @DisplayName("generate()")
    class Generate {

        @Test
        @DisplayName("Should generate distinct OrderItemId instances on each call")
        void shouldGenerateDistinctOrderItemIds() {
            OrderItemId first = OrderItemId.generate();
            OrderItemId second = OrderItemId.generate();

            assertNotEquals(first, second);
        }
    }
}
