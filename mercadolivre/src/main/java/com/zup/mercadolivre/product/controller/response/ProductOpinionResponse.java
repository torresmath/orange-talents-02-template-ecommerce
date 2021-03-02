package com.zup.mercadolivre.product.controller.response;

import com.zup.mercadolivre.product.model.ProductOpinion;

public class ProductOpinionResponse {

    private final int rating;

    private final String title;

    private final String description;

    public ProductOpinionResponse(ProductOpinion productOpinion) {
        this.rating = productOpinion.getRating();
        this.title = productOpinion.getTitle();
        this.description = productOpinion.getDescription();
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
