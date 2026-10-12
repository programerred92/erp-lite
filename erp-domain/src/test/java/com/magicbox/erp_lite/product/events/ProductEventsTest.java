package com.magicbox.erp_lite.product.events;

import com.magicbox.erp_lite.domain.product.ProductId;
import com.magicbox.erp_lite.domain.product.ProductName;
import com.magicbox.erp_lite.domain.product.SKU;
import com.magicbox.erp_lite.domain.product.events.ProductCreated;
import com.magicbox.erp_lite.domain.product.events.ProductDeactivated;
import com.magicbox.erp_lite.domain.product.events.ProductUpdated;
import com.magicbox.erp_lite.domain.product.events.StockChanged;
import com.magicbox.erp_lite.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product Domain Events Test")
class ProductEventsTest {

    private static final ProductId PRODUCT_ID = ProductId.generate();
    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("ProductCreated")
    class ProductCreatedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            SKU sku = SKU.of("LAPTOP-001");
            ProductName name = ProductName.of("Laptop Pro");
            Money price = Money.of(new BigDecimal("999.99"), Currency.getInstance("USD"));

            ProductCreated event = new ProductCreated(PRODUCT_ID, sku, name, price, NOW);

            assertEquals(PRODUCT_ID, event.productId());
            assertEquals(sku, event.sku());
            assertEquals(name, event.name());
            assertEquals(price, event.price());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("ProductUpdated")
    class ProductUpdatedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            ProductUpdated event = new ProductUpdated(PRODUCT_ID, NOW);

            assertEquals(PRODUCT_ID, event.productId());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("ProductDeactivated")
    class ProductDeactivatedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            ProductDeactivated event = new ProductDeactivated(PRODUCT_ID, NOW);

            assertEquals(PRODUCT_ID, event.productId());
            assertEquals(NOW, event.timestamp());
        }
    }

    @Nested
    @DisplayName("StockChanged")
    class StockChangedTest {

        @Test
        @DisplayName("Should expose all provided attributes")
        void shouldExposeAllAttributes() {
            StockChanged event = new StockChanged(PRODUCT_ID, 10, 15, "restock", NOW);

            assertEquals(PRODUCT_ID, event.productId());
            assertEquals(10, event.oldStock());
            assertEquals(15, event.newStock());
            assertEquals("restock", event.reason());
            assertEquals(NOW, event.timestamp());
        }
    }
}
