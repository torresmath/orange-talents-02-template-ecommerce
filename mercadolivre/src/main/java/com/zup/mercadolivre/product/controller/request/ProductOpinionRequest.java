package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductOpinion;
import com.zup.mercadolivre.user.model.User;
import io.jsonwebtoken.lang.Assert;

import javax.validation.constraints.*;

public class ProductOpinionRequest {

    @Min(1)
    @Max(5)
    @NotNull
    private final int rating;

    @NotNull
    @NotBlank
    private final String title;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 500)
    private final String description;

    public ProductOpinionRequest(@Min(1) @Max(5) @NotNull int rating,
                                 @NotNull @NotBlank String title,
                                 @NotNull @NotBlank @Size(min = 1, max = 500) String description) {
        this.rating = rating;
        this.title = title;
        this.description = description;
    }

    public ProductOpinion toModel(Product product, User customer) {

        Assert.notNull(customer, "Impossível cadastrar opinião para produto sem um cliente válido");
        Assert.notNull(product, "Impossível cadastrar opinião sem produto válido");

        return new ProductOpinion(rating, title, description, customer, product);
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
