package com.zup.mercadolivre.product.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.product.event.SaveProductImageEvent;
import com.zup.mercadolivre.product.model.Product;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;

public class ProductImageRequest {

    @JsonProperty("images")
    @NotEmpty
    private List<MultipartFile> images;

    @JsonIgnore
    private Product product;

    public SaveProductImageEvent toEvent(Product product) {

        this.product = product;
        Assert.isTrue(this.product != null, "Impossível salvar imagem sem produto ou link");
        Assert.isTrue(!images.isEmpty(), "Impossível salvar imagens sem nenhuma URL");

        return new SaveProductImageEvent(this);
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public Product getProduct() {
        return product;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = Collections.unmodifiableList(images);
    }
}
