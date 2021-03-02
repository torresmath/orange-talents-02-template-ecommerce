 package com.zup.mercadolivre.product.controller.response;

import com.zup.mercadolivre.product.model.ProductDetail;

public class ProductViewDetailResponse {

    public final String name;
    public final String description;

    public ProductViewDetailResponse(ProductDetail productDetail) {
        this.name = productDetail.getName();
        this.description = productDetail.getDescription();
    }
}
