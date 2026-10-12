package com.magicbox.erp_lite.order.events;

import com.magicbox.erp_lite.domain.order.OrderId;
import com.magicbox.erp_lite.domain.order.events.OrderCancelled;
import com.magicbox.erp_lite.domain.order.events.OrderConfirmed;
import com.magicbox.erp_lite.domain.order.events.OrderCreated;
import com.magicbox.erp_lite.domain.order.events.OrderDelivered;
import com.magicbox.erp_lite.domain.order.events.OrderShipped;
import com.magicbox.erp_lite.domain.shared.CustomerId;
import com.magicbox.erp_lite.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Order Domain Events Test")
class OrderEventsTest {

    private static final OrderId ORDER_ID = OrderId.generate();
    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("OrderCreated")
    class OrderCreatedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            CustomerId customerId = CustomerId.of(1L);
            Money totalAmount = Money.of(new BigDecimal("100.00"), Currency.getInstance("USD"));

            OrderCreated event = new OrderCreated(ORDER_ID, customerId, "John Doe", totalAmount, NOW);

            assertEquals(ORDER_ID, event.orderId());
            assertEquals(customerId, event.customerId());
            assertEquals("John Doe", event.customerName());
            assertEquals(totalAmount, event.totalAmount());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("OrderConfirmed")
    class OrderConfirmedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            OrderConfirmed event = new OrderConfirmed(ORDER_ID, NOW);

            assertEquals(ORDER_ID, event.orderId());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("OrderShipped")
    class OrderShippedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            OrderShipped event = new OrderShipped(ORDER_ID, NOW);

            assertEquals(ORDER_ID, event.orderId());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("OrderDelivered")
    class OrderDeliveredTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            OrderDelivered event = new OrderDelivered(ORDER_ID, NOW);

            assertEquals(ORDER_ID, event.orderId());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("OrderCancelled")
    class OrderCancelledTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            OrderCancelled event = new OrderCancelled(ORDER_ID, "customer request", NOW);

            assertEquals(ORDER_ID, event.orderId());
            assertEquals("customer request", event.reason());
            assertEquals(NOW, event.timestamp());
        }
    }
}
