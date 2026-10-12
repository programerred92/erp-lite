package com.magicbox.erp_lite.domain.product.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitted when stock changes (increment or decrement). Triggers sync to MongoDB.
 */
public record StockChanged(ProductId productId, Integer oldStock, Integer newStock, String reason, Instant timestamp)
        implements DomainEvent {
}
