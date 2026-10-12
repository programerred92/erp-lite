package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Guard.notNull(value, "productId");
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
