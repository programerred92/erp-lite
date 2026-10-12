package com.magicbox.erp_lite.domain.common;

import java.util.Objects;

/**
 * Base class for domain entities. Identity is defined by the ID returned from {@link #getId()}.
 *
 * @param <ID> identifier type
 */
public abstract class Entity<ID> {

    protected final ID id;

    protected Entity(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity ID cannot be null");
        }
        this.id = id;
    }
    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ID id = this.id;
        return id.equals(((Entity<?>) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
