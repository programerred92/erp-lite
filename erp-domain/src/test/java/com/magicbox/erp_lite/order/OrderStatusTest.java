package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OrderStatus Domain Test")
class OrderStatusTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderStatus(null));

            assertEquals("orderStatus must not be null", exception.getMessage());
        }

        @ParameterizedTest(name = "value=[{0}] should throw IllegalArgumentException")
        @ValueSource(strings = {"UNKNOWN", "pending", ""})
        @DisplayName("Should throw IllegalArgumentException when value is not a valid status")
        void shouldThrowWhenValueIsInvalid(String invalidValue) {
            assertThrows(IllegalArgumentException.class, () -> new OrderStatus(invalidValue));
        }

        @ParameterizedTest(name = "value=[{0}] should be accepted")
        @ValueSource(strings = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"})
        @DisplayName("Should create an OrderStatus for every valid status value")
        void shouldCreateOrderStatusForValidValue(String validValue) {
            OrderStatus status = new OrderStatus(validValue);

            assertEquals(validValue, status.value());
        }
    }

    @Nested
    @DisplayName("Static factory methods")
    class StaticFactories {

        @Test
        @DisplayName("pending() should create a PENDING status")
        void shouldCreatePendingStatus() {
            assertTrue(OrderStatus.pending().isPending());
        }

        @Test
        @DisplayName("confirmed() should create a CONFIRMED status")
        void shouldCreateConfirmedStatus() {
            assertTrue(OrderStatus.confirmed().isConfirmed());
        }

        @Test
        @DisplayName("shipped() should create a SHIPPED status")
        void shouldCreateShippedStatus() {
            assertTrue(OrderStatus.shipped().isShipped());
        }

        @Test
        @DisplayName("delivered() should create a DELIVERED status")
        void shouldCreateDeliveredStatus() {
            assertTrue(OrderStatus.delivered().isDelivered());
        }

        @Test
        @DisplayName("cancelled() should create a CANCELLED status")
        void shouldCreateCancelledStatus() {
            assertTrue(OrderStatus.cancelled().isCancelled());
        }

        @Test
        @DisplayName("of(value) should create an OrderStatus from the given value")
        void shouldCreateOrderStatusUsingFactory() {
            OrderStatus status = OrderStatus.of("PENDING");

            assertEquals("PENDING", status.value());
        }
    }

    @Nested
    @DisplayName("canTransitionTo(nextStatus)")
    class CanTransitionTo {

        @Test
        @DisplayName("Should throw IllegalArgumentException when nextStatus is null")
        void shouldThrowWhenNextStatusIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderStatus.pending().canTransitionTo(null));

            assertEquals("nextStatus must not be null", exception.getMessage());
        }

        @ParameterizedTest(name = "{0} -> {1} should be allowed")
        @CsvSource({
                "PENDING, CONFIRMED",
                "PENDING, CANCELLED",
                "CONFIRMED, SHIPPED",
                "CONFIRMED, CANCELLED",
                "SHIPPED, DELIVERED"
        })
        @DisplayName("Should allow valid status transitions")
        void shouldAllowValidTransitions(String from, String to) {
            assertTrue(OrderStatus.of(from).canTransitionTo(OrderStatus.of(to)));
        }

        @ParameterizedTest(name = "{0} -> {1} should not be allowed")
        @CsvSource({
                "PENDING, SHIPPED",
                "PENDING, DELIVERED",
                "CONFIRMED, PENDING",
                "CONFIRMED, DELIVERED",
                "SHIPPED, PENDING",
                "SHIPPED, CANCELLED",
                "DELIVERED, CANCELLED",
                "CANCELLED, PENDING"
        })
        @DisplayName("Should not allow invalid status transitions")
        void shouldNotAllowInvalidTransitions(String from, String to) {
            assertFalse(OrderStatus.of(from).canTransitionTo(OrderStatus.of(to)));
        }
    }

    @Nested
    @DisplayName("Status predicates")
    class StatusPredicates {

        @Test
        @DisplayName("isPending() should return false for non-pending statuses")
        void isPendingShouldReturnFalseForOtherStatuses() {
            assertFalse(OrderStatus.confirmed().isPending());
        }

        @Test
        @DisplayName("isConfirmed() should return false for non-confirmed statuses")
        void isConfirmedShouldReturnFalseForOtherStatuses() {
            assertFalse(OrderStatus.pending().isConfirmed());
        }

        @Test
        @DisplayName("isShipped() should return false for non-shipped statuses")
        void isShippedShouldReturnFalseForOtherStatuses() {
            assertFalse(OrderStatus.pending().isShipped());
        }

        @Test
        @DisplayName("isDelivered() should return false for non-delivered statuses")
        void isDeliveredShouldReturnFalseForOtherStatuses() {
            assertFalse(OrderStatus.pending().isDelivered());
        }

        @Test
        @DisplayName("isCancelled() should return false for non-cancelled statuses")
        void isCancelledShouldReturnFalseForOtherStatuses() {
            assertFalse(OrderStatus.pending().isCancelled());
        }
    }

    @Nested
    @DisplayName("isFinalState()")
    class IsFinalState {

        @ParameterizedTest(name = "{0} should be a final state")
        @ValueSource(strings = {"DELIVERED", "CANCELLED"})
        @DisplayName("Should return true for DELIVERED and CANCELLED statuses")
        void shouldReturnTrueForFinalStates(String value) {
            assertTrue(OrderStatus.of(value).isFinalState());
        }

        @ParameterizedTest(name = "{0} should not be a final state")
        @ValueSource(strings = {"PENDING", "CONFIRMED", "SHIPPED"})
        @DisplayName("Should return false for non-final statuses")
        void shouldReturnFalseForNonFinalStates(String value) {
            assertFalse(OrderStatus.of(value).isFinalState());
        }
    }
}
