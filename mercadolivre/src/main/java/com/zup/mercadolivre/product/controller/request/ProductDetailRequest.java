package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.product.model.ProductDetail;

import javax.validation.constraints.NotBlank;

public class ProductDetailRequest {

    @NotBlank
    String name;
    @NotBlank
    String description;

    public ProductDetailRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetail toModel() {
        return new ProductDetail(name, description);
    }
}
