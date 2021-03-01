package com.zup.mercadolivre.product.event;

import com.zup.mercadolivre.product.controller.request.ProductImageRequest;
import com.zup.mercadolivre.product.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

public class SaveProductImageEvent {

    private final ProductImageRequest imageRequest;

    public List<MultipartFile> getImages() {
        return Collections.unmodifiableList(imageRequest.getImages());
    }

    public Product getProduct() {
        return imageRequest.getProduct();
    }

    public SaveProductImageEvent(ProductImageRequest imageRequest) {
        this.imageRequest = imageRequest;
    }
}
