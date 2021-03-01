package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductQuestion;
import com.zup.mercadolivre.user.model.User;
import io.jsonwebtoken.lang.Assert;

import javax.validation.constraints.NotBlank;

public class ProductQuestionRequest {

    @NotBlank
    private String title;

    public ProductQuestionRequest() {
        super();
    }

    public ProductQuestionRequest(@NotBlank String title) {
        this.title = title;
    }


    public ProductQuestion toModel(Product product, User customer) {

        Assert.notNull(product, "Impossível criar pergunta para produto inválido");
        Assert.notNull(customer, "Impossível criar pergunta para produto com cliente inválido");

        return new ProductQuestion(title, customer, product);
    }

    public String getTitle() {
        return title;
    }
}
