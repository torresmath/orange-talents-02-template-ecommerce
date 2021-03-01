package com.zup.mercadolivre.product.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.product.model.ProductQuestion;

import java.time.LocalDateTime;

public class ProductQuestionResponse {

    private final Long id;

    private final String title;

    private final String customer;

    @JsonProperty("create_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private final LocalDateTime createDate;

    public ProductQuestionResponse(ProductQuestion productQuestion) {

        this.id = productQuestion.getId();
        this.title = productQuestion.getTitle();
        this.customer = productQuestion.getCustomer();
        this.createDate = productQuestion.getCreateDate();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCustomer() {
        return customer;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
