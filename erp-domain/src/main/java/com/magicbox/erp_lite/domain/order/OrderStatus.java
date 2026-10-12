package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.Map;
import java.util.Set;

/**
 * Order state with valid transitions. DELIVERED and CANCELLED are final states.
 */
public record OrderStatus(String value) {

    private static final String PENDING = "PENDING";
    private static final String CONFIRMED = "CONFIRMED";
    private static final String SHIPPED = "SHIPPED";
    private static final String DELIVERED = "DELIVERED";
    private static final String CANCELLED = "CANCELLED";

    private static final Map<String, Set<String>> TRANSITIONS = Map.of(
            PENDING, Set.of(CONFIRMED, CANCELLED),
            CONFIRMED, Set.of(SHIPPED, CANCELLED),
            SHIPPED, Set.of(DELIVERED),
            DELIVERED, Set.of(),
            CANCELLED, Set.of());

    public OrderStatus {
        Guard.notNull(value, "orderStatus");
        if (!TRANSITIONS.containsKey(value)) {
            throw new IllegalArgumentException(
                    "Invalid order status: " + value + ". Allowed: PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED");
        }
    }

    public static OrderStatus of(String value) {
        return new OrderStatus(value);
    }

    public static OrderStatus pending() {
        return new OrderStatus(PENDING);
    }

    public static OrderStatus confirmed() {
        return new OrderStatus(CONFIRMED);
    }

    public static OrderStatus shipped() {
        return new OrderStatus(SHIPPED);
    }

    public static OrderStatus delivered() {
        return new OrderStatus(DELIVERED);
    }

    public static OrderStatus cancelled() {
        return new OrderStatus(CANCELLED);
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        Guard.notNull(nextStatus, "nextStatus");
        return TRANSITIONS.get(value).contains(nextStatus.value);
    }

    public boolean isPending() {
        return PENDING.equals(value);
    }

    public boolean isConfirmed() {
        return CONFIRMED.equals(value);
    }

    public boolean isShipped() {
        return SHIPPED.equals(value);
    }

    public boolean isDelivered() {
        return DELIVERED.equals(value);
    }

    public boolean isCancelled() {
        return CANCELLED.equals(value);
    }

    public boolean isFinalState() {
        return TRANSITIONS.get(value).isEmpty();
    }
}
