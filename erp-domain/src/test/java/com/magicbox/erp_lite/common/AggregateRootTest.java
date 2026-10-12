package com.magicbox.erp_lite.common;

import com.magicbox.erp_lite.domain.common.AggregateRoot;
import com.magicbox.erp_lite.domain.common.DomainEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AggregateRoot Domain Test")
class AggregateRootTest {

    private record TestEvent(String name) implements DomainEvent {
    }

    private static class TestAggregate extends AggregateRoot<String> {
        TestAggregate(String id) {
            super(id);
        }

        void raise(DomainEvent event) {
            registerEvent(event);
        }
    }

    @Nested
    @DisplayName("registerEvent(event)")
    class RegisterEvent {

        @Test
        @DisplayName("Should throw IllegalArgumentException when event is null")
        void shouldThrowWhenEventIsNull() {
            TestAggregate aggregate = new TestAggregate("id-1");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> aggregate.raise(null));

            assertEquals("event must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should add the event to the pending domain events")
        void shouldAddEventToPendingList() {
            TestAggregate aggregate = new TestAggregate("id-1");
            TestEvent event = new TestEvent("created");

            aggregate.raise(event);

            assertEquals(List.of(event), aggregate.getDomainEvents());
        }
    }

    @Nested
    @DisplayName("getDomainEvents()")
    class GetDomainEvents {

        @Test
        @DisplayName("Should return an empty list when no events were registered")
        void shouldReturnEmptyListByDefault() {
            TestAggregate aggregate = new TestAggregate("id-1");

            assertTrue(aggregate.getDomainEvents().isEmpty());
        }

        @Test
        @DisplayName("Should return an unmodifiable list")
        void shouldReturnUnmodifiableList() {
            TestAggregate aggregate = new TestAggregate("id-1");
            aggregate.raise(new TestEvent("created"));

            List<DomainEvent> events = aggregate.getDomainEvents();

            assertThrows(UnsupportedOperationException.class, () -> events.add(new TestEvent("other")));
        }
    }

    @Nested
    @DisplayName("pullDomainEvents()")
    class PullDomainEvents {

        @Test
        @DisplayName("Should return the registered events and clear the internal list")
        void shouldReturnEventsAndClearList() {
            TestAggregate aggregate = new TestAggregate("id-1");
            TestEvent event = new TestEvent("created");
            aggregate.raise(event);

            List<DomainEvent> pulled = aggregate.pullDomainEvents();

            assertEquals(List.of(event), pulled);
            assertTrue(aggregate.getDomainEvents().isEmpty());
        }

        @Test
        @DisplayName("Should return an empty list when there are no pending events")
        void shouldReturnEmptyListWhenNoEvents() {
            TestAggregate aggregate = new TestAggregate("id-1");

            List<DomainEvent> pulled = aggregate.pullDomainEvents();

            assertTrue(pulled.isEmpty());
        }
    }

    @Nested
    @DisplayName("clearDomainEvents()")
    class ClearDomainEvents {

        @Test
        @DisplayName("Should remove all pending events without returning them")
        void shouldRemoveAllPendingEvents() {
            TestAggregate aggregate = new TestAggregate("id-1");
            aggregate.raise(new TestEvent("created"));

            aggregate.clearDomainEvents();

            assertTrue(aggregate.getDomainEvents().isEmpty());
        }
    }
}
