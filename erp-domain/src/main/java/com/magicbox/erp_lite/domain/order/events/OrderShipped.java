package com.magicbox.erp_lite.domain.order.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.order.OrderId;

import java.time.Instant;

/**
 * Emitted when order transitions CONFIRMED -> SHIPPED.
 */
public record OrderShipped(OrderId orderId, Instant timestamp) implements DomainEvent {
}
