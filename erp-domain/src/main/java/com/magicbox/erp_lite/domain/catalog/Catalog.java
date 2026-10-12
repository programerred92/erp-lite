package com.magicbox.erp_lite.domain.catalog;

import com.magicbox.erp_lite.domain.common.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Catalog extends AggregateRoot<String> {

    private final CatalogType catalogType;
    private final String name;
    private final String description;
    private final List<CatalogItem> items;
    private boolean isActive;

    public Catalog(String id, String name, String description, CatalogType catalogType, List<CatalogItem> items, boolean isActive) {
        super(id);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (catalogType == null || catalogType.getCode() == null || catalogType.getCode().isBlank()) {
            throw new IllegalArgumentException("catalogType must not be null or blank");
        }
        this.catalogType = catalogType;
        this.name = name;
        this.description = description;
        this.items = items;
        this.isActive = isActive;
    }

  public Optional<CatalogItem> findItemByCode(String code) {
        return items.stream().filter(item -> item.getCode().equals(code)).findFirst();
  }

  public boolean containsItem(String code) {
        return this.findItemByCode(code).isPresent();
  }

  public List<CatalogItem> findItemsActive() {
        return this.items.stream().filter(CatalogItem::isActive).toList();
  }

  public List<CatalogItem> findAllItems() {
        return Collections.unmodifiableList(this.items);
  }
}
