package com.magicbox.erp_lite.common;

import com.magicbox.erp_lite.domain.common.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Entity Domain Test")
class EntityTest {

    private static class TestEntity extends Entity<String> {
        TestEntity(String id) {
            super(id);
        }
    }

    private static class OtherEntity extends Entity<String> {
        OtherEntity(String id) {
            super(id);
        }
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when id is null")
        void shouldThrowWhenIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new TestEntity(null));

            assertEquals("Entity ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should store the provided id")
        void shouldStoreProvidedId() {
            TestEntity entity = new TestEntity("id-1");

            assertEquals("id-1", entity.getId());
        }
    }

    @Nested
    @DisplayName("equals and hashCode contracts")
    class EqualsAndHashCode {

        @Test
        @DisplayName("Should be equal to itself (reflexivity)")
        void shouldBeEqualToItself() {
            TestEntity entity = new TestEntity("id-1");

            assertEquals(entity, entity);
        }

        @Test
        @DisplayName("Should be equal to another entity of the same type with the same id")
        void shouldBeEqualWhenSameTypeAndId() {
            TestEntity first = new TestEntity("id-1");
            TestEntity second = new TestEntity("id-1");

            assertEquals(first, second);
        }

        @Test
        @DisplayName("Should not be equal when ids are different")
        void shouldNotBeEqualWhenIdsDiffer() {
            TestEntity first = new TestEntity("id-1");
            TestEntity second = new TestEntity("id-2");

            assertNotEquals(first, second);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            TestEntity entity = new TestEntity("id-1");

            assertNotEquals(null, entity);
        }

        @Test
        @DisplayName("Should not be equal to an instance of a different type with the same id")
        void shouldNotBeEqualToDifferentType() {
            TestEntity entity = new TestEntity("id-1");
            OtherEntity other = new OtherEntity("id-1");

            assertFalse(entity.equals(other));
        }

        @Test
        @DisplayName("Should produce the same hashCode for equal ids")
        void shouldProduceSameHashCodeForEqualIds() {
            TestEntity first = new TestEntity("id-1");
            TestEntity second = new TestEntity("id-1");

            assertEquals(first.hashCode(), second.hashCode());
        }

        @Test
        @DisplayName("Should produce consistent hashCode across multiple invocations")
        void shouldProduceConsistentHashCode() {
            TestEntity entity = new TestEntity("id-1");

            assertTrue(entity.hashCode() == entity.hashCode());
        }
    }
}
