package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Guard;

import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        Guard.notNull(value, "orderId");
    }

    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
