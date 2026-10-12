package com.magicbox.erp_lite.catalog;

import com.magicbox.erp_lite.domain.catalog.Catalog;
import com.magicbox.erp_lite.domain.catalog.CatalogItem;
import com.magicbox.erp_lite.domain.catalog.CatalogType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Catalog Domain Test")
class CatalogTest {

    private static final String VALID_ID = UUID.randomUUID().toString();
    private static final String VALID_NAME = "Product Categories";
    private static final String VALID_DESCRIPTION = "Catalog description";

    private static CatalogItem newItem(String code, boolean active) {
        CatalogItem item = new CatalogItem(UUID.randomUUID().toString(), code, "value", "description", 1, null);
        if (!active) {
            item.turnOffStatus();
        }
        return item;
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void shouldThrowWhenNameIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Catalog(VALID_ID, null, VALID_DESCRIPTION, CatalogType.PRODUCT_CATEGORIES,
                            List.of(), true));

            assertEquals("name must not be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is blank")
        void shouldThrowWhenNameIsBlank() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Catalog(VALID_ID, "  ", VALID_DESCRIPTION, CatalogType.PRODUCT_CATEGORIES,
                            List.of(), true));

            assertEquals("name must not be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when catalogType is null")
        void shouldThrowWhenCatalogTypeIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION, null, List.of(), true));

            assertEquals("catalogType must not be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when entity id is null (inherited from Entity)")
        void shouldThrowWhenIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Catalog(null, VALID_NAME, VALID_DESCRIPTION, CatalogType.PRODUCT_CATEGORIES,
                            List.of(), true));

            assertEquals("Entity ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a Catalog with all provided attributes correctly set")
        void shouldCreateCatalogWithValidData() {
            List<CatalogItem> items = List.of(newItem("ITEM-1", true));

            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, items, true);

            assertEquals(VALID_ID, catalog.getId());
            assertEquals(VALID_NAME, catalog.getName());
            assertEquals(VALID_DESCRIPTION, catalog.getDescription());
            assertEquals(CatalogType.PRODUCT_CATEGORIES, catalog.getCatalogType());
            assertEquals(items, catalog.getItems());
            assertTrue(catalog.isActive());
        }
    }

    @Nested
    @DisplayName("findItemByCode(code)")
    class FindItemByCode {

        @Test
        @DisplayName("Should return the item when the code exists")
        void shouldReturnItemWhenCodeExists() {
            CatalogItem item = newItem("ITEM-1", true);
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(item), true);

            assertEquals(item, catalog.findItemByCode("ITEM-1").orElseThrow());
        }

        @Test
        @DisplayName("Should return empty Optional when the code does not exist")
        void shouldReturnEmptyWhenCodeDoesNotExist() {
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(newItem("ITEM-1", true)), true);

            assertTrue(catalog.findItemByCode("MISSING").isEmpty());
        }
    }

    @Nested
    @DisplayName("containsItem(code)")
    class ContainsItem {

        @Test
        @DisplayName("Should return true when the item exists")
        void shouldReturnTrueWhenItemExists() {
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(newItem("ITEM-1", true)), true);

            assertTrue(catalog.containsItem("ITEM-1"));
        }

        @Test
        @DisplayName("Should return false when the item does not exist")
        void shouldReturnFalseWhenItemDoesNotExist() {
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(newItem("ITEM-1", true)), true);

            assertFalse(catalog.containsItem("MISSING"));
        }
    }

    @Nested
    @DisplayName("findItemsActive()")
    class FindItemsActive {

        @Test
        @DisplayName("Should return only the active items")
        void shouldReturnOnlyActiveItems() {
            CatalogItem activeItem = newItem("ITEM-1", true);
            CatalogItem inactiveItem = newItem("ITEM-2", false);
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(activeItem, inactiveItem), true);

            assertEquals(List.of(activeItem), catalog.findItemsActive());
        }

        @Test
        @DisplayName("Should return an empty list when there are no active items")
        void shouldReturnEmptyListWhenNoActiveItems() {
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(newItem("ITEM-1", false)), true);

            assertTrue(catalog.findItemsActive().isEmpty());
        }
    }

    @Nested
    @DisplayName("findAllItems()")
    class FindAllItems {

        @Test
        @DisplayName("Should return all items regardless of status")
        void shouldReturnAllItems() {
            CatalogItem activeItem = newItem("ITEM-1", true);
            CatalogItem inactiveItem = newItem("ITEM-2", false);
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(activeItem, inactiveItem), true);

            assertEquals(List.of(activeItem, inactiveItem), catalog.findAllItems());
        }

        @Test
        @DisplayName("Should return an unmodifiable list")
        void shouldReturnUnmodifiableList() {
            List<CatalogItem> items = new ArrayList<>(List.of(newItem("ITEM-1", true)));
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, items, true);

            List<CatalogItem> result = catalog.findAllItems();

            assertThrows(UnsupportedOperationException.class, () -> result.add(newItem("ITEM-2", true)));
        }
    }

    @Nested
    @DisplayName("equals, hashCode and toString contracts")
    class ObjectContracts {

        @Test
        @DisplayName("Should be equal to another catalog with the same id and the same attributes")
        void shouldBeEqualWhenSameIdAndAttributes() {
            Catalog first = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(), true);
            Catalog second = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(), true);

            assertEquals(first, second);
            assertEquals(first.hashCode(), second.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to another catalog with the same id but different attributes")
        void shouldNotBeEqualWhenSameIdButDifferentAttributes() {
            Catalog first = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(), true);
            Catalog second = new Catalog(VALID_ID, "Other name", "Other description",
                    CatalogType.ORDER_STATUSES, List.of(), false);

            assertNotEquals(first, second);
        }

        @Test
        @DisplayName("Should produce a non-null toString representation including the name")
        void shouldProduceToStringContainingName() {
            Catalog catalog = new Catalog(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                    CatalogType.PRODUCT_CATEGORIES, List.of(), true);

            String result = catalog.toString();

            assertTrue(result.contains(VALID_NAME));
        }
    }
}
