package com.magicbox.erp_lite.domain.product.events;

import com.magicbox.erp_lite.domain.common.DomainEvent;
import com.magicbox.erp_lite.domain.product.ProductId;
import com.magicbox.erp_lite.domain.product.ProductName;
import com.magicbox.erp_lite.domain.product.SKU;
import com.magicbox.erp_lite.domain.shared.Money;

import java.time.Instant;

/**
 * Emitted when a new product is created. Triggers sync to MongoDB (CQRS).
 */
public record ProductCreated(ProductId productId, SKU sku, ProductName name, Money price, Instant timestamp)
        implements DomainEvent {
}
