package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.UUID;

public record OrderItemId(UUID value) {

    public OrderItemId {
        Guard.notNull(value, "orderItemId");
    }

    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }

    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }
}
