package com.magicbox.erp_lite.shared;

import com.magicbox.erp_lite.domain.shared.AuditInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AuditInfo Domain Test")
class AuditInfoTest {

    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "createdBy=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when createdBy is null or blank")
        void shouldThrowWhenCreatedByIsNullOrBlank(String invalidCreatedBy) {
            assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo(invalidCreatedBy, NOW, NOW));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when createdAt is null")
        void shouldThrowWhenCreatedAtIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo("user", null, NOW));

            assertEquals("createdAt must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when updatedAt is null")
        void shouldThrowWhenUpdatedAtIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo("user", NOW, null));

            assertEquals("updatedAt must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when updatedAt is before createdAt")
        void shouldThrowWhenUpdatedAtIsBeforeCreatedAt() {
            Instant createdAt = NOW;
            Instant updatedAt = NOW.minus(1, ChronoUnit.DAYS);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo("user", createdAt, updatedAt));

            assertEquals("updatedAt must not be before createdAt", exception.getMessage());
        }

        @Test
        @DisplayName("Should create AuditInfo when updatedAt equals createdAt")
        void shouldCreateAuditInfoWhenUpdatedAtEqualsCreatedAt() {
            AuditInfo auditInfo = new AuditInfo("user", NOW, NOW);

            assertEquals(NOW, auditInfo.createdAt());
            assertEquals(NOW, auditInfo.updatedAt());
        }

        @Test
        @DisplayName("Should create AuditInfo when updatedAt is after createdAt")
        void shouldCreateAuditInfoWhenUpdatedAtIsAfterCreatedAt() {
            Instant updatedAt = NOW.plus(1, ChronoUnit.DAYS);

            AuditInfo auditInfo = new AuditInfo("user", NOW, updatedAt);

            assertEquals(updatedAt, auditInfo.updatedAt());
        }
    }

    @Nested
    @DisplayName("create(createdBy, timestamp)")
    class CreateFactory {

        @Test
        @DisplayName("Should create an AuditInfo with createdAt and updatedAt equal to the given timestamp")
        void shouldCreateAuditInfoWithSameTimestamps() {
            AuditInfo auditInfo = AuditInfo.create("user", NOW);

            assertEquals("user", auditInfo.createdBy());
            assertEquals(NOW, auditInfo.createdAt());
            assertEquals(NOW, auditInfo.updatedAt());
        }
    }

    @Nested
    @DisplayName("updateTimestamp()")
    class UpdateTimestamp {

        @Test
        @DisplayName("Should return a new AuditInfo keeping createdBy and createdAt unchanged")
        void shouldKeepCreatedByAndCreatedAtUnchanged() {
            Instant createdAt = NOW.minus(1, ChronoUnit.DAYS);
            AuditInfo auditInfo = AuditInfo.create("user", createdAt);

            AuditInfo updated = auditInfo.updateTimestamp();

            assertEquals("user", updated.createdBy());
            assertEquals(createdAt, updated.createdAt());
            assertTrue(!updated.updatedAt().isBefore(createdAt));
        }

        @Test
        @DisplayName("Should not allow updatedAt to be before createdAt even when createdAt is in the future")
        void shouldNotAllowUpdatedAtBeforeCreatedAtWhenCreatedAtIsInTheFuture() {
            Instant futureCreatedAt = NOW.plus(10, ChronoUnit.DAYS);
            AuditInfo auditInfo = AuditInfo.create("user", futureCreatedAt);

            AuditInfo updated = auditInfo.updateTimestamp();

            assertEquals(futureCreatedAt, updated.updatedAt());
            assertFalse(updated.updatedAt().isBefore(updated.createdAt()));
        }
    }
}
