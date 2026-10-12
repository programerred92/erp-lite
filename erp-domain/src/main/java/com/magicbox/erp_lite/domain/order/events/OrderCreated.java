package com.magicbox.erp_lite.domain.order.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.order.OrderId;
import com.magicbox.erp_lite.domain.shared.CustomerId;
import com.magicbox.erp_lite.domain.shared.Money;

import java.time.Instant;

/**
 * Emitted when a new order is created.
 */
public record OrderCreated(OrderId orderId, CustomerId customerId, String customerName, Money totalAmount,
                           Instant timestamp) implements DomainEvent {
}
