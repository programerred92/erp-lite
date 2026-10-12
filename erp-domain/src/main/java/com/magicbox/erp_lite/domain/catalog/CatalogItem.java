package com.magicbox.erp_lite.domain.catalog;

import com.magicbox.erp_lite.domain.common.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CatalogItem extends Entity<String> {
    String id;
    private final String code;
    private final String value;
    private final String description;
    private final int displayOrder;
    private final Map<String, Object> metadata;
    private boolean isActive;

    public CatalogItem(String id, String code, String value, String description, int displayOrder, Map<String, Object> metadata) {

        super(id);

        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be null or blank");
        }

        this.code = code;
        this.value = value;
        this.description = description;
        this.displayOrder = displayOrder;
        this.metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        this.isActive = true;
    }

    @Override
    public String getId() {
        return this.code;
    }

    public void turnOffStatus() {
        this.isActive = false;
    }

    public void turnOnStatus() {
        this.isActive = true;
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public  boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }
}
