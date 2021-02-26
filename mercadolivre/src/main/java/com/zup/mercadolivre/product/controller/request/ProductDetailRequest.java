package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import io.jsonwebtoken.lang.Assert;

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

    public ProductDetail toModel(Product product) {

        Assert.isTrue(product != null, "Impossível criar detalhes de um produto sem produto");

        return new ProductDetail(name, description, product);
    }
}
