package com.magicbox.erp_lite.domain.repository;

import com.magicbox.erp_lite.domain.catalog.Catalog;
import com.magicbox.erp_lite.domain.catalog.CatalogItem;
import com.magicbox.erp_lite.domain.catalog.CatalogType;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    Optional<Catalog> findByType(CatalogType type);
    List<CatalogItem> findItemsByCatalogType(CatalogType type);
    Optional<CatalogItem> findItemByTypeAndCode(CatalogType type, String code);
}
