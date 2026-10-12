package com.magicbox.erp_lite.domain.order.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.order.OrderId;

import java.time.Instant;

/**
 * Emitted when an order is cancelled. If it was CONFIRMED, stock must be released.
 */
public record OrderCancelled(OrderId orderId, String reason, Instant timestamp) implements DomainEvent {
}
