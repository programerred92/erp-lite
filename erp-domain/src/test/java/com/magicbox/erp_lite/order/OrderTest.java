package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.order.Customer;
import com.magicbox.erp_lite.domain.order.Order;
import com.magicbox.erp_lite.domain.order.OrderItem;
import com.magicbox.erp_lite.domain.order.OrderNumber;
import com.magicbox.erp_lite.domain.order.OrderStatus;
import com.magicbox.erp_lite.domain.order.events.OrderCancelled;
import com.magicbox.erp_lite.domain.order.events.OrderConfirmed;
import com.magicbox.erp_lite.domain.order.events.OrderCreated;
import com.magicbox.erp_lite.domain.order.events.OrderDelivered;
import com.magicbox.erp_lite.domain.order.events.OrderShipped;
import com.magicbox.erp_lite.domain.product.CategoryReference;
import com.magicbox.erp_lite.domain.product.Product;
import com.magicbox.erp_lite.domain.product.ProductImage;
import com.magicbox.erp_lite.domain.product.ProductName;
import com.magicbox.erp_lite.domain.product.SKU;
import com.magicbox.erp_lite.domain.product.Stock;
import com.magicbox.erp_lite.domain.shared.CustomerId;
import com.magicbox.erp_lite.domain.shared.Money;
import com.magicbox.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Order Domain Test")
class OrderTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Customer VALID_CUSTOMER = Customer.of(CustomerId.of(1L), "John Doe");

    private static Product product(String sku, String price, Currency currency) {
        return Product.create(
                SKU.of(sku),
                ProductName.of("Product " + sku),
                "description",
                Money.of(new BigDecimal(price), currency),
                Stock.of(100),
                CategoryReference.of("cat-electronics"),
                ProductImage.of("https://bucket.s3.amazonaws.com/" + sku + ".png"),
                "admin");
    }

    private static OrderItem item(String sku, String price, int quantity) {
        return OrderItem.from(product(sku, price, USD), Quantity.of(quantity));
    }

    private static Order newPendingOrder(OrderItem... items) {
        return Order.create(OrderNumber.generate(), VALID_CUSTOMER, new ArrayList<>(List.of(items)), "admin");
    }

    @Nested
    @DisplayName("create(...)")
    class Create {

        @Test
        @DisplayName("Should create an order in PENDING status with the computed total amount")
        void shouldCreateOrderInPendingStatus() {
            OrderItem orderItem = item("LAPTOP-001", "100.00", 2);

            Order order = Order.create(OrderNumber.generate(), VALID_CUSTOMER, List.of(orderItem), "admin");

            assertTrue(order.getStatus().isPending());
            assertEquals(new BigDecimal("200.00"), order.getTotalAmount().amount());
            assertEquals(List.of(orderItem), order.getItems());
        }

        @Test
        @DisplayName("Should register an OrderCreated domain event")
        void shouldRegisterOrderCreatedEvent() {
            OrderItem orderItem = item("LAPTOP-001", "100.00", 1);

            Order order = Order.create(OrderNumber.generate(), VALID_CUSTOMER, List.of(orderItem), "admin");

            List<DomainEvent> events = order.getDomainEvents();
            assertEquals(1, events.size());
            OrderCreated event = assertInstanceOf(OrderCreated.class, events.get(0));
            assertEquals(order.getId(), event.orderId());
            assertEquals(VALID_CUSTOMER.customerId(), event.customerId());
            assertEquals(VALID_CUSTOMER.customerName(), event.customerName());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when orderNumber is null")
        void shouldThrowWhenOrderNumberIsNull() {
            OrderItem orderItem = item("LAPTOP-001", "100.00", 1);

            assertThrows(IllegalArgumentException.class,
                    () -> Order.create(null, VALID_CUSTOMER, List.of(orderItem), "admin"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when customer is null")
        void shouldThrowWhenCustomerIsNull() {
            OrderItem orderItem = item("LAPTOP-001", "100.00", 1);

            assertThrows(IllegalArgumentException.class,
                    () -> Order.create(OrderNumber.generate(), null, List.of(orderItem), "admin"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items list is empty")
        void shouldThrowWhenItemsIsEmpty() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Order.create(OrderNumber.generate(), VALID_CUSTOMER, List.of(), "admin"));

            assertEquals("An order must contain at least one item", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items list contains a null item")
        void shouldThrowWhenItemsContainsNull() {
            List<OrderItem> items = new ArrayList<>();
            items.add(null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Order.create(OrderNumber.generate(), VALID_CUSTOMER, items, "admin"));

            assertEquals("item must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items have different currencies")
        void shouldThrowWhenItemsHaveDifferentCurrencies() {
            OrderItem usdItem = OrderItem.from(product("LAPTOP-001", "100.00", USD), Quantity.of(1));
            OrderItem eurItem = OrderItem.from(product("MOUSE-002", "20.00", EUR), Quantity.of(1));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Order.create(OrderNumber.generate(), VALID_CUSTOMER, List.of(usdItem, eurItem), "admin"));

            assertEquals("All items in an order must share the same currency", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("confirm()")
    class Confirm {

        @Test
        @DisplayName("Should transition from PENDING to CONFIRMED and register an OrderConfirmed event")
        void shouldTransitionToConfirmed() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.pullDomainEvents();

            order.confirm();

            assertTrue(order.getStatus().isConfirmed());
            List<DomainEvent> events = order.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(OrderConfirmed.class, events.get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is not PENDING")
        void shouldThrowWhenOrderIsNotPending() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.confirm();

            IllegalStateException exception = assertThrows(IllegalStateException.class, order::confirm);

            assertEquals("Invalid order status transition: CONFIRMED -> CONFIRMED", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("ship()")
    class Ship {

        @Test
        @DisplayName("Should transition from CONFIRMED to SHIPPED and register an OrderShipped event")
        void shouldTransitionToShipped() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.confirm();
            order.pullDomainEvents();

            order.ship();

            assertTrue(order.getStatus().isShipped());
            List<DomainEvent> events = order.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(OrderShipped.class, events.get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is still PENDING")
        void shouldThrowWhenOrderIsPending() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));

            assertThrows(IllegalStateException.class, order::ship);
        }
    }

    @Nested
    @DisplayName("deliver()")
    class Deliver {

        @Test
        @DisplayName("Should transition from SHIPPED to DELIVERED and register an OrderDelivered event")
        void shouldTransitionToDelivered() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.confirm();
            order.ship();
            order.pullDomainEvents();

            order.deliver();

            assertTrue(order.getStatus().isDelivered());
            List<DomainEvent> events = order.getDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(OrderDelivered.class, events.get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is not SHIPPED")
        void shouldThrowWhenOrderIsNotShipped() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));

            assertThrows(IllegalStateException.class, order::deliver);
        }
    }

    @Nested
    @DisplayName("cancel(reason)")
    class Cancel {

        @Test
        @DisplayName("Should transition to CANCELLED and register an OrderCancelled event")
        void shouldTransitionToCancelled() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.pullDomainEvents();

            order.cancel("customer request");

            assertTrue(order.getStatus().isCancelled());
            List<DomainEvent> events = order.getDomainEvents();
            assertEquals(1, events.size());
            OrderCancelled event = assertInstanceOf(OrderCancelled.class, events.get(0));
            assertEquals("customer request", event.reason());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowWhenReasonIsBlank() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));

            assertThrows(IllegalArgumentException.class, () -> order.cancel(" "));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is already DELIVERED")
        void shouldThrowWhenOrderIsDelivered() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.confirm();
            order.ship();
            order.deliver();

            assertThrows(IllegalStateException.class, () -> order.cancel("too late"));
        }
    }

    @Nested
    @DisplayName("addItem(item)")
    class AddItem {

        @Test
        @DisplayName("Should add the item and recalculate the total amount")
        void shouldAddItemAndRecalculateTotal() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            OrderItem newItem = item("MOUSE-002", "20.00", 1);

            order.addItem(newItem);

            assertEquals(2, order.getItems().size());
            assertEquals(new BigDecimal("120.00"), order.getTotalAmount().amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is null")
        void shouldThrowWhenItemIsNull() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));

            assertThrows(IllegalArgumentException.class, () -> order.addItem(null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the new item has a different currency")
        void shouldThrowWhenCurrencyDiffers() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            OrderItem eurItem = OrderItem.from(product("MOUSE-002", "20.00", EUR), Quantity.of(1));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> order.addItem(eurItem));

            assertEquals("All items in an order must share the same currency", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is not PENDING")
        void shouldThrowWhenOrderIsNotPending() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));
            order.confirm();
            OrderItem newItem = item("MOUSE-002", "20.00", 1);

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> order.addItem(newItem));

            assertEquals("Cannot add items when order is CONFIRMED; order must be PENDING", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("removeItem(item)")
    class RemoveItem {

        @Test
        @DisplayName("Should remove the item and recalculate the total amount")
        void shouldRemoveItemAndRecalculateTotal() {
            OrderItem firstItem = item("LAPTOP-001", "100.00", 1);
            OrderItem secondItem = item("MOUSE-002", "20.00", 1);
            Order order = newPendingOrder(firstItem, secondItem);

            order.removeItem(secondItem);

            assertEquals(List.of(firstItem), order.getItems());
            assertEquals(new BigDecimal("100.00"), order.getTotalAmount().amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is null")
        void shouldThrowWhenItemIsNull() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1), item("MOUSE-002", "20.00", 1));

            assertThrows(IllegalArgumentException.class, () -> order.removeItem(null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is not part of the order")
        void shouldThrowWhenItemNotFound() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1), item("MOUSE-002", "20.00", 1));
            OrderItem unrelatedItem = item("KEYBOARD-003", "50.00", 1);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> order.removeItem(unrelatedItem));

            assertEquals("Item not found in order: " + unrelatedItem.getId(), exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when removing the last remaining item")
        void shouldThrowWhenRemovingLastItem() {
            OrderItem onlyItem = item("LAPTOP-001", "100.00", 1);
            Order order = newPendingOrder(onlyItem);

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> order.removeItem(onlyItem));

            assertEquals("An order must contain at least one item", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when the order is not PENDING")
        void shouldThrowWhenOrderIsNotPending() {
            OrderItem firstItem = item("LAPTOP-001", "100.00", 1);
            OrderItem secondItem = item("MOUSE-002", "20.00", 1);
            Order order = newPendingOrder(firstItem, secondItem);
            order.confirm();

            assertThrows(IllegalStateException.class, () -> order.removeItem(secondItem));
        }
    }

    @Nested
    @DisplayName("calculateTotal()")
    class CalculateTotal {

        @Test
        @DisplayName("Should return the sum of all item subtotals")
        void shouldReturnSumOfSubtotals() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 2), item("MOUSE-002", "20.00", 3));

            Money total = order.calculateTotal();

            assertEquals(new BigDecimal("260.00"), total.amount());
        }
    }

    @Nested
    @DisplayName("getItems()")
    class GetItems {

        @Test
        @DisplayName("Should return an unmodifiable view of the order items")
        void shouldReturnUnmodifiableList() {
            Order order = newPendingOrder(item("LAPTOP-001", "100.00", 1));

            List<OrderItem> items = order.getItems();

            assertThrows(UnsupportedOperationException.class,
                    () -> items.add(item("MOUSE-002", "20.00", 1)));
        }
    }
}
