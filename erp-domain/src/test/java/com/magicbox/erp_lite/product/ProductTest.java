package com.magicbox.erp_lite.product;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.product.CategoryReference;
import com.magicbox.erp_lite.domain.product.Product;
import com.magicbox.erp_lite.domain.product.ProductImage;
import com.magicbox.erp_lite.domain.product.ProductName;
import com.magicbox.erp_lite.domain.product.SKU;
import com.magicbox.erp_lite.domain.product.Stock;
import com.magicbox.erp_lite.domain.product.events.ProductCreated;
import com.magicbox.erp_lite.domain.product.events.ProductDeactivated;
import com.magicbox.erp_lite.domain.product.events.ProductUpdated;
import com.magicbox.erp_lite.domain.product.events.StockChanged;
import com.magicbox.erp_lite.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Product Domain Test")
class ProductTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final SKU VALID_SKU = SKU.of("LAPTOP-001");
    private static final ProductName VALID_NAME = ProductName.of("Laptop Pro");
    private static final String VALID_DESCRIPTION = "A powerful laptop";
    private static final Money VALID_PRICE = Money.of(new BigDecimal("999.99"), USD);
    private static final Stock VALID_STOCK = Stock.of(10);
    private static final CategoryReference VALID_CATEGORY = CategoryReference.of("cat-electronics");
    private static final ProductImage VALID_IMAGE = ProductImage.of("https://bucket.s3.amazonaws.com/laptop.png");
    private static final String VALID_CREATED_BY = "admin";

    private static Product newProduct() {
        return Product.create(VALID_SKU, VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK,
                VALID_CATEGORY, VALID_IMAGE, VALID_CREATED_BY);
    }

    @Nested
    @DisplayName("create(...)")
    class Create {

        @Test
        @DisplayName("Should create a Product with all provided attributes correctly set")
        void shouldCreateProductWithValidData() {
            Product product = newProduct();

            assertEquals(VALID_SKU, product.getSku());
            assertEquals(VALID_NAME, product.getName());
            assertEquals(VALID_DESCRIPTION, product.getDescription());
            assertEquals(VALID_PRICE, product.getPrice());
            assertEquals(VALID_STOCK, product.getStock());
            assertEquals(VALID_CATEGORY, product.getCategory());
            assertEquals(VALID_IMAGE, product.getImage());
            assertTrue(product.isActive());
        }

        @Test
        @DisplayName("Should register a ProductCreated domain event")
        void shouldRegisterProductCreatedEvent() {
            Product product = newProduct();

            List<DomainEvent> events = product.getDomainEvents();

            assertEquals(1, events.size());
            ProductCreated event = assertInstanceOf(ProductCreated.class, events.get(0));
            assertEquals(product.getId(), event.productId());
            assertEquals(VALID_SKU, event.sku());
            assertEquals(VALID_NAME, event.name());
            assertEquals(VALID_PRICE, event.price());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when price is not positive")
        void shouldThrowWhenPriceIsNotPositive() {
            Money zeroPrice = Money.zero(USD);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Product.create(VALID_SKU, VALID_NAME, VALID_DESCRIPTION, zeroPrice, VALID_STOCK,
                            VALID_CATEGORY, VALID_IMAGE, VALID_CREATED_BY));

            assertEquals("price must be > 0", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("update(...)")
    class Update {

        @Test
        @DisplayName("Should update name, description, price, category and image")
        void shouldUpdateProductAttributes() {
            Product product = newProduct();
            ProductName newName = ProductName.of("Laptop Ultra");
            Money newPrice = Money.of(new BigDecimal("1200.00"), USD);
            CategoryReference newCategory = CategoryReference.of("cat-premium");
            ProductImage newImage = ProductImage.of("https://bucket.s3.amazonaws.com/laptop-ultra.png");

            product.update(newName, "Updated description", newPrice, newCategory, newImage);

            assertEquals(newName, product.getName());
            assertEquals("Updated description", product.getDescription());
            assertEquals(newPrice, product.getPrice());
            assertEquals(newCategory, product.getCategory());
            assertEquals(newImage, product.getImage());
        }

        @Test
        @DisplayName("Should register a ProductUpdated domain event")
        void shouldRegisterProductUpdatedEvent() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.update(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_CATEGORY, VALID_IMAGE);

            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(ProductUpdated.class, events.get(0));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when new price is not positive")
        void shouldThrowWhenNewPriceIsNotPositive() {
            Product product = newProduct();
            Money zeroPrice = Money.zero(USD);

            assertThrows(IllegalArgumentException.class,
                    () -> product.update(VALID_NAME, VALID_DESCRIPTION, zeroPrice, VALID_CATEGORY, VALID_IMAGE));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when new name is null")
        void shouldThrowWhenNewNameIsNull() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class,
                    () -> product.update(null, VALID_DESCRIPTION, VALID_PRICE, VALID_CATEGORY, VALID_IMAGE));
        }
    }

    @Nested
    @DisplayName("incrementStock(quantity, reason)")
    class IncrementStock {

        @Test
        @DisplayName("Should increase the stock value")
        void shouldIncreaseStockValue() {
            Product product = newProduct();

            product.incrementStock(5, "restock");

            assertEquals(15, product.getStock().value());
        }

        @Test
        @DisplayName("Should register a StockChanged domain event with old and new stock values")
        void shouldRegisterStockChangedEvent() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.incrementStock(5, "restock");

            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            StockChanged event = assertInstanceOf(StockChanged.class, events.get(0));
            assertEquals(10, event.oldStock());
            assertEquals(15, event.newStock());
            assertEquals("restock", event.reason());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowWhenReasonIsBlank() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.incrementStock(5, " "));
        }
    }

    @Nested
    @DisplayName("decrementStock(quantity, reason)")
    class DecrementStock {

        @Test
        @DisplayName("Should decrease the stock value")
        void shouldDecreaseStockValue() {
            Product product = newProduct();

            product.decrementStock(4, "sale");

            assertEquals(6, product.getStock().value());
        }

        @Test
        @DisplayName("Should register a StockChanged domain event with old and new stock values")
        void shouldRegisterStockChangedEvent() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.decrementStock(4, "sale");

            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            StockChanged event = assertInstanceOf(StockChanged.class, events.get(0));
            assertEquals(10, event.oldStock());
            assertEquals(6, event.newStock());
            assertEquals("sale", event.reason());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when stock is insufficient")
        void shouldThrowWhenStockIsInsufficient() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.decrementStock(100, "sale"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowWhenReasonIsBlank() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.decrementStock(4, ""));
        }
    }

    @Nested
    @DisplayName("changePrice(newPrice)")
    class ChangePrice {

        @Test
        @DisplayName("Should update the product price")
        void shouldUpdatePrice() {
            Product product = newProduct();
            Money newPrice = Money.of(new BigDecimal("850.00"), USD);

            product.changePrice(newPrice);

            assertEquals(newPrice, product.getPrice());
        }

        @Test
        @DisplayName("Should register a ProductUpdated domain event")
        void shouldRegisterProductUpdatedEvent() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.changePrice(Money.of(new BigDecimal("850.00"), USD));

            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(ProductUpdated.class, events.get(0));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the new price is not positive")
        void shouldThrowWhenNewPriceIsNotPositive() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.changePrice(Money.zero(USD)));
        }
    }

    @Nested
    @DisplayName("deactivate()")
    class Deactivate {

        @Test
        @DisplayName("Should mark the product as inactive and register a ProductDeactivated event")
        void shouldDeactivateProduct() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.deactivate();

            assertFalse(product.isActive());
            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(ProductDeactivated.class, events.get(0));
        }

        @Test
        @DisplayName("Should do nothing when the product is already inactive")
        void shouldDoNothingWhenAlreadyInactive() {
            Product product = newProduct();
            product.deactivate();
            product.pullDomainEvents();

            product.deactivate();

            assertFalse(product.isActive());
            assertTrue(product.getDomainEvents().isEmpty());
        }
    }

    @Nested
    @DisplayName("activate()")
    class Activate {

        @Test
        @DisplayName("Should mark the product as active and register a ProductUpdated event")
        void shouldActivateProduct() {
            Product product = newProduct();
            product.deactivate();
            product.pullDomainEvents();

            product.activate();

            assertTrue(product.isActive());
            List<DomainEvent> events = product.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(ProductUpdated.class, events.get(0));
        }

        @Test
        @DisplayName("Should do nothing when the product is already active")
        void shouldDoNothingWhenAlreadyActive() {
            Product product = newProduct();
            product.pullDomainEvents();

            product.activate();

            assertTrue(product.isActive());
            assertTrue(product.getDomainEvents().isEmpty());
        }
    }

    @Nested
    @DisplayName("hasAvailableStock(requiredQuantity)")
    class HasAvailableStock {

        @Test
        @DisplayName("Should return true when stock covers the required quantity")
        void shouldReturnTrueWhenStockIsSufficient() {
            Product product = newProduct();

            assertTrue(product.hasAvailableStock(5));
        }

        @Test
        @DisplayName("Should return false when stock does not cover the required quantity")
        void shouldReturnFalseWhenStockIsInsufficient() {
            Product product = newProduct();

            assertFalse(product.hasAvailableStock(100));
        }
    }
}
