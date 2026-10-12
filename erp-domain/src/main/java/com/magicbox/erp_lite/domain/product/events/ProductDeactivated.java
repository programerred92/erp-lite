package com.magicbox.erp_lite.domain.product.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitted when a product is deactivated. Triggers sync to MongoDB.
 */
public record ProductDeactivated(ProductId productId, Instant timestamp) implements DomainEvent {
}
