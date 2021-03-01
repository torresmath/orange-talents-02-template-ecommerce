package com.zup.mercadolivre.product.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zup.mercadolivre.product.event.SaveProductImageEvent;
import com.zup.mercadolivre.product.model.Product;
import io.jsonwebtoken.lang.Assert;

import java.util.Collections;
import java.util.List;

@JsonDeserialize(as = ProductImageRequest.class)
public class ProductImageRequest {

    @JsonProperty("images")
    private List<String> images;

    @JsonIgnore
    private Product product;

    public ProductImageRequest() {
        super();
    }

    public ProductImageRequest(List<String> images) {
        this.images = images;
    }

    public SaveProductImageEvent toEvent(Product product) {

        this.product = product;
        Assert.isTrue(this.product != null, "Impossível salvar imagem sem produto ou link");
        Assert.isTrue(!images.isEmpty(), "Impossível salvar imagens sem nenhuma URL");

        return new SaveProductImageEvent(this);
    }

    public List<String> getImages() {
        return Collections.unmodifiableList(images);
    }

    public Product getProduct() {
        return product;
    }
}
