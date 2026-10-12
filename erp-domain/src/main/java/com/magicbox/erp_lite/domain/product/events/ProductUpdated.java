package com.magicbox.erp_lite.domain.product.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitted when product info is updated. Triggers sync to MongoDB.
 */
public record ProductUpdated(ProductId productId, Instant timestamp) implements DomainEvent {
}
