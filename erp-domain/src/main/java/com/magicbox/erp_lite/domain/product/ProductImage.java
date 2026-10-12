package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.Guard;

import java.net.URI;

/**
 * Product image URL (stored in AWS S3). Must be an absolute http(s) URL.
 */
public record ProductImage(String imageUrl) {

    public ProductImage {
        Guard.notBlank(imageUrl, "imageUrl");
        imageUrl = imageUrl.strip();
        URI uri;
        try {
            uri = URI.create(imageUrl);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
        String scheme = uri.getScheme();
        boolean httpScheme = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        if (!httpScheme || uri.getHost() == null) {
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
    }

    public static ProductImage of(String imageUrl) {
        return new ProductImage(imageUrl);
    }

    public String getFullUrl() {
        return imageUrl;
    }

    public String getFileName() {
        String path = URI.create(imageUrl).getPath();
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
