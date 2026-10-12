package com.magicbox.erp_lite.catalog;

import com.magicbox.erp_lite.domain.catalog.CatalogItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CatalogItem Domain Test")
class CatalogItemTest {

    public static final String MESSAGE_EXCEPTION = "code must not be null or blank";

    private static final String VALID_ID = UUID.randomUUID().toString();
    private static final String VALID_CODE = "ITEM_CODE";
    private static final String VALID_VALUE = "Some value";
    private static final String VALID_DESCRIPTION = "Description of test";
    private static final int VALID_DISPLAY_ORDER = 1;

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @ParameterizedTest(name = "code=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
        @DisplayName("Should throw IllegalArgumentException when code is null, empty or blank")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsNullOrBlank(String invalidCode) {
            IllegalArgumentException targetException = assertThrows(IllegalArgumentException.class, () ->
                    new CatalogItem(
                            VALID_ID,
                            invalidCode,
                            VALID_VALUE,
                            VALID_DESCRIPTION,
                            VALID_DISPLAY_ORDER,
                            Map.of("type", "electronic")
                    ));

            assertEquals(MESSAGE_EXCEPTION, targetException.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when entity id is null (inherited from Entity)")
        void shouldThrowIllegalArgumentExceptionWhenEntityIdIsNull() {
            IllegalArgumentException targetException = assertThrows(IllegalArgumentException.class, () ->
                    new CatalogItem(
                            null,
                            VALID_CODE,
                            VALID_VALUE,
                            VALID_DESCRIPTION,
                            VALID_DISPLAY_ORDER,
                            Map.of("type", "electronic")
                    ));

            assertEquals("Entity ID cannot be null", targetException.getMessage());
        }

        @Test
        @DisplayName("Should create a CatalogItem with all provided attributes correctly set")
        void shouldCreateCatalogItemWithValidData() {
            Map<String, Object> metadata = Map.of("type", "electronic");

            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, metadata);

            assertAll(
                    () -> assertEquals(VALID_CODE, catalogItem.getCode()),
                    () -> assertEquals(VALID_VALUE, catalogItem.getValue()),
                    () -> assertEquals(VALID_DESCRIPTION, catalogItem.getDescription()),
                    () -> assertEquals(VALID_DISPLAY_ORDER, catalogItem.getDisplayOrder()),
                    () -> assertEquals(metadata, catalogItem.getMetadata()),
                    () -> assertTrue(catalogItem.isActive())
            );
        }

        @Test
        @DisplayName("Should default metadata to an empty map when null is provided")
        void shouldDefaultMetadataToEmptyMapWhenNull() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertNotNull(catalogItem.getMetadata());
            assertTrue(catalogItem.getMetadata().isEmpty());
        }

        @Test
        @DisplayName("Should store an immutable defensive copy of the provided metadata")
        void shouldStoreDefensiveImmutableCopyOfMetadata() {
            Map<String, Object> mutableMetadata = new HashMap<>();
            mutableMetadata.put("type", "electronic");

            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, mutableMetadata);

            // Mutating the original map after construction must not affect the stored metadata.
            mutableMetadata.put("brand", "acme");

            assertFalse(catalogItem.hasMetadata("brand"));
            assertThrows(UnsupportedOperationException.class,
                    () -> catalogItem.getMetadata().put("another", "value"));
        }

        @Test
        @DisplayName("Should initialize isActive as true by default")
        void shouldInitializeAsActiveByDefault() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertTrue(catalogItem.isActive());
        }
    }

    @Nested
    @DisplayName("getId() behavior")
    class GetIdBehavior {

        @Test
        @DisplayName("Should return the business code as the entity id, not the raw constructor id")
        void shouldReturnCodeAsId() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertEquals(VALID_CODE, catalogItem.getId());
            assertNotEquals(VALID_ID, catalogItem.getId());
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        @Test
        @DisplayName("Should turn status off when turnOffStatus is invoked")
        void shouldTurnOffStatus() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            catalogItem.turnOffStatus();

            assertFalse(catalogItem.isActive());
        }

        @Test
        @DisplayName("Should turn status on when turnOnStatus is invoked after being turned off")
        void shouldTurnOnStatus() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            catalogItem.turnOffStatus();
            catalogItem.turnOnStatus();

            assertTrue(catalogItem.isActive());
        }

        @Test
        @DisplayName("Should remain active when turnOnStatus is invoked while already active")
        void shouldRemainActiveWhenTurnOnStatusCalledWhileActive() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            catalogItem.turnOnStatus();

            assertTrue(catalogItem.isActive());
        }

        @Test
        @DisplayName("Should remain inactive when turnOffStatus is invoked twice")
        void shouldRemainInactiveWhenTurnOffStatusCalledTwice() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            catalogItem.turnOffStatus();
            catalogItem.turnOffStatus();

            assertFalse(catalogItem.isActive());
        }
    }

    @Nested
    @DisplayName("Metadata access")
    class MetadataAccess {

        @Test
        @DisplayName("Should return the metadata value for an existing key")
        void shouldReturnMetadataValueForExistingKey() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER,
                    Map.of("type", "electronic"));

            assertEquals("electronic", catalogItem.getMetadata("type"));
        }

        @Test
        @DisplayName("Should return null when the metadata key does not exist")
        void shouldReturnNullForMissingMetadataKey() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER,
                    Map.of("type", "electronic"));

            assertNull(catalogItem.getMetadata("non-existent-key"));
        }

        @Test
        @DisplayName("Should return true when hasMetadata is called with an existing key")
        void shouldReturnTrueWhenMetadataKeyExists() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER,
                    Map.of("type", "electronic"));

            assertTrue(catalogItem.hasMetadata("type"));
        }

        @Test
        @DisplayName("Should return false when hasMetadata is called with a non-existing key")
        void shouldReturnFalseWhenMetadataKeyDoesNotExist() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER,
                    Map.of("type", "electronic"));

            assertFalse(catalogItem.hasMetadata("non-existent-key"));
        }

        @Test
        @DisplayName("Should return false for hasMetadata and null for getMetadata when metadata map is empty")
        void shouldHandleEmptyMetadataGracefully() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertFalse(catalogItem.hasMetadata("type"));
            assertNull(catalogItem.getMetadata("type"));
        }
    }

    @Nested
    @DisplayName("equals, hashCode and toString contracts")
    class ObjectContracts {

        @Test
        @DisplayName("Should be equal to itself (reflexivity)")
        void shouldBeEqualToItself() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertEquals(catalogItem, catalogItem);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertNotEquals(null, catalogItem);
        }

        @Test
        @DisplayName("Should not be equal to an instance of a different type")
        void shouldNotBeEqualToDifferentType() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertNotEquals("a string", catalogItem);
        }

        @Test
        @DisplayName("Should produce a consistent hashCode across multiple invocations")
        void shouldProduceConsistentHashCode() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            assertEquals(catalogItem.hashCode(), catalogItem.hashCode());
        }

        @Test
        @DisplayName("Should produce a non-null toString representation including the code")
        void shouldProduceToStringContainingCode() {
            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID, VALID_CODE, VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, null);

            String result = catalogItem.toString();

            assertNotNull(result);
            assertTrue(result.contains(VALID_CODE));
        }
    }
}
