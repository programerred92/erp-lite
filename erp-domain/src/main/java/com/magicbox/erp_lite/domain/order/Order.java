package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.AggregateRoot;
import com.magicbox.erp_lite.domain.common.Guard;
import com.magicbox.erp_lite.domain.order.events.OrderCancelled;
import com.magicbox.erp_lite.domain.order.events.OrderConfirmed;
import com.magicbox.erp_lite.domain.order.events.OrderCreated;
import com.magicbox.erp_lite.domain.order.events.OrderDelivered;
import com.magicbox.erp_lite.domain.order.events.OrderShipped;
import com.magicbox.erp_lite.domain.shared.AuditInfo;
import com.magicbox.erp_lite.domain.shared.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Order extends AggregateRoot<OrderId> {

    private OrderId id;
    private OrderNumber orderNumber;
    private Customer customer;
    private OrderStatus status;
    private List<OrderItem> items;
    private Money totalAmount;
    private AuditInfo auditInfo;

    Order(OrderId id, OrderNumber orderNumber, Customer customer, OrderStatus status,
          List<OrderItem> items, AuditInfo auditInfo) {
        super(id);
        this.orderNumber = Guard.notNull(orderNumber, "orderNumber");
        this.customer = Guard.notNull(customer, "customer");
        this.status = Guard.notNull(status, "status");
        this.items = new ArrayList<>(Guard.notNull(items, "items"));
        this.auditInfo = Guard.notNull(auditInfo, "auditInfo");
        validateItems();
        this.totalAmount = sumSubtotals();
    }

    public static Order create(OrderNumber orderNumber, Customer customer, List<OrderItem> items,
                               String createdBy) {
        Instant now = Instant.now();
        Order order = new Order(OrderId.generate(), orderNumber, customer, OrderStatus.pending(),
                items, AuditInfo.create(createdBy, now));
        order.registerEvent(new OrderCreated(order.id, customer.customerId(), customer.customerName(),
                order.totalAmount, now));
        return order;
    }

    public void confirm() {
        transitionTo(OrderStatus.confirmed());
        registerEvent(new OrderConfirmed(id, auditInfo.updatedAt()));
    }

    public void ship() {
        transitionTo(OrderStatus.shipped());
        registerEvent(new OrderShipped(id, auditInfo.updatedAt()));
    }

    public void deliver() {
        transitionTo(OrderStatus.delivered());
        registerEvent(new OrderDelivered(id, auditInfo.updatedAt()));
    }

    public void cancel(String reason) {
        Guard.notBlank(reason, "reason");
        transitionTo(OrderStatus.cancelled());
        registerEvent(new OrderCancelled(id, reason, auditInfo.updatedAt()));
    }

    public void addItem(OrderItem item) {
        Guard.notNull(item, "item");
        requirePending("add items");
        if (!item.getUnitPrice().hasSameCurrency(totalAmount)) {
            throw new IllegalArgumentException("All items in an order must share the same currency");
        }
        items.add(item);
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    public void removeItem(OrderItem item) {
        Guard.notNull(item, "item");
        requirePending("remove items");
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not found in order: " + item.getId());
        }
        if (items.size() == 1) {
            throw new IllegalStateException("An order must contain at least one item");
        }
        items.remove(item);
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    public Money calculateTotal() {
        this.totalAmount = sumSubtotals();
        return totalAmount;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void validateItems() {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one item");
        }
        items.forEach(item -> Guard.notNull(item, "item"));
        Money first = items.get(0).getUnitPrice();
        for (OrderItem item : items) {
            if (!item.getUnitPrice().hasSameCurrency(first)) {
                throw new IllegalArgumentException("All items in an order must share the same currency");
            }
        }
    }

    private void validateTransition(OrderStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException(
                    "Invalid order status transition: " + status.value() + " -> " + next.value());
        }
    }

    private void transitionTo(OrderStatus next) {
        validateTransition(next);
        this.status = next;
        this.auditInfo = auditInfo.updateTimestamp();
    }

    private void requirePending(String action) {
        if (!status.isPending()) {
            throw new IllegalStateException(
                    "Cannot " + action + " when order is " + status.value() + "; order must be PENDING");
        }
    }

    private Money sumSubtotals() {
        Money total = Money.zero(items.get(0).getUnitPrice().currency());
        for (OrderItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
}
