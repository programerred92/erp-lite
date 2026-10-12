package com.magicbox.erp_lite.domain.shared;

import com.magicbox.erp_lite.domain.common.Guard;

import java.time.Instant;

/**
 * Audit information for aggregates.
 */
public record AuditInfo(String createdBy, Instant createdAt, Instant updatedAt) {

    public AuditInfo {
        Guard.notBlank(createdBy, "createdBy");
        Guard.notNull(createdAt, "createdAt");
        Guard.notNull(updatedAt, "updatedAt");
        if (updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("updatedAt must not be before createdAt");
        }
    }

    public static AuditInfo create(String createdBy, Instant timestamp) {
        return new AuditInfo(createdBy, timestamp, timestamp);
    }

    public AuditInfo updateTimestamp() {
        Instant now = Instant.now();
        return new AuditInfo(createdBy, createdAt, now.isBefore(createdAt) ? createdAt : now);
    }
}
