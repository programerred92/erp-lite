package com.magicbox.erp_lite.domain.ports;

import com.magicbox.erp_lite.domain.product.ProductImage;

public interface ImagesStorageService {

    ProductImage upload(String imageName, byte[] imageData);
    void delete(ProductImage img);
    byte[] download(ProductImage img);
}
