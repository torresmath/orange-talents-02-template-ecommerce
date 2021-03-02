package com.zup.mercadolivre.product.controller.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductImage;

import java.math.BigDecimal;
import java.util.Set;
import java.util.SortedSet;

@JsonPropertyOrder({
        "name",
        "amount",
        "price",
        "description",
        "details",
        "opinions",
        "questions",
        "images"
})
public class ProductViewResponse {

    private final String name;

    private final int amount;

    private final BigDecimal price;

    private final Set<ProductViewDetailResponse> details;

    private final String description;

    private final ProductViewOpinionsResponse opinions;

    private final SortedSet<ProductQuestionResponse> questions;

    private final Set<String> images;

    public ProductViewResponse(Product product) {
        this.name = product.getName();
        this.amount = product.getAmount();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.details = product.mapDetails(ProductViewDetailResponse::new);
        this.opinions = new ProductViewOpinionsResponse(product.getOpinions());
        this.questions = product.mapQuestions(ProductQuestionResponse::new);
        this.images = product.mapImages(ProductImage::getLink);
    }

    public String getName() { return name; }

    public int getAmount() { return amount; }

    public BigDecimal getPrice() { return price; }

    public Set<ProductViewDetailResponse> getDetails() { return details; }

    public String getDescription() { return description; }

    public ProductViewOpinionsResponse getOpinions() { return opinions; }

    public SortedSet<ProductQuestionResponse> getQuestions() { return questions; }

    public Set<String> getImages() { return images; }
}
