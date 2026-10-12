package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.OrderItem;
import com.magicbox.erp_lite.domain.product.CategoryReference;
import com.magicbox.erp_lite.domain.product.Product;
import com.magicbox.erp_lite.domain.product.ProductImage;
import com.magicbox.erp_lite.domain.product.ProductName;
import com.magicbox.erp_lite.domain.product.SKU;
import com.magicbox.erp_lite.domain.product.Stock;
import com.magicbox.erp_lite.domain.shared.Money;
import com.magicbox.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OrderItem Domain Test")
class OrderItemTest {

    private static final Currency USD = Currency.getInstance("USD");

    private static Product newActiveProduct() {
        return Product.create(
                SKU.of("LAPTOP-001"),
                ProductName.of("Laptop Pro"),
                "A powerful laptop",
                Money.of(new BigDecimal("999.99"), USD),
                Stock.of(10),
                CategoryReference.of("cat-electronics"),
                ProductImage.of("https://bucket.s3.amazonaws.com/laptop.png"),
                "admin");
    }

    @Nested
    @DisplayName("from(product, quantity)")
    class From {

        @Test
        @DisplayName("Should throw IllegalArgumentException when product is null")
        void shouldThrowWhenProductIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderItem.from(null, Quantity.of(1)));

            assertEquals("product must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity is null")
        void shouldThrowWhenQuantityIsNull() {
            Product product = newActiveProduct();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderItem.from(product, null));

            assertEquals("quantity must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the product is not active")
        void shouldThrowWhenProductIsNotActive() {
            Product product = newActiveProduct();
            product.deactivate();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderItem.from(product, Quantity.of(1)));

            assertEquals("Product is not active: " + product.getSku().value(), exception.getMessage());
        }

        @Test
        @DisplayName("Should create an OrderItem snapshotting the product name and price")
        void shouldCreateOrderItemSnapshottingProductData() {
            Product product = newActiveProduct();
            Quantity quantity = Quantity.of(3);

            OrderItem item = OrderItem.from(product, quantity);

            assertEquals(product.getId(), item.getProductReference());
            assertEquals(product.getName().value(), item.getProductName());
            assertEquals(quantity, item.getQuantity());
            assertEquals(product.getPrice(), item.getUnitPrice());
            assertEquals(product.getPrice().multiply(quantity), item.getSubtotal());
        }
    }

    @Nested
    @DisplayName("calculateSubtotal()")
    class CalculateSubtotal {

        @Test
        @DisplayName("Should return the unitPrice multiplied by the quantity")
        void shouldReturnUnitPriceMultipliedByQuantity() {
            Product product = newActiveProduct();
            OrderItem item = OrderItem.from(product, Quantity.of(2));

            Money subtotal = item.calculateSubtotal();

            assertEquals(product.getPrice().multiply(2), subtotal);
        }
    }

    @Nested
    @DisplayName("Protected no-arg constructor")
    class NoArgConstructor {

        private static class TestableOrderItem extends OrderItem {
            TestableOrderItem() {
                super();
            }
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException because entity id cannot be null")
        void shouldThrowWhenInstantiatedWithoutId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    TestableOrderItem::new);

            assertEquals("Entity ID cannot be null", exception.getMessage());
        }
    }
}
