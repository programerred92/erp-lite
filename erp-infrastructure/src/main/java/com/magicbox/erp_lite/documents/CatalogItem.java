package com.magicbox.erp_lite.documents;

public record CatalogItem(String id, String code, String value, String description, Integer displayOrder,
                          CatalogItemMetadata metadata) {
}
