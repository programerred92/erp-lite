package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.Guard;

/**
 * Reference to a catalog category stored in MongoDB. Example: cat-electronics
 */
public record CategoryReference(String categoryId) {

    public CategoryReference {
        Guard.notBlank(categoryId, "categoryId");
    }

    public static CategoryReference of(String categoryId) {
        return new CategoryReference(categoryId);
    }
}
