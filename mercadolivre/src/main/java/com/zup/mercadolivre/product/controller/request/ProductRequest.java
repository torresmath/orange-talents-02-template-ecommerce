package com.zup.mercadolivre.product.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.common.annotations.ExistsId;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import com.zup.mercadolivre.user.model.User;
import io.jsonwebtoken.lang.Assert;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRequest {

    @NotBlank
    String name;

    @NotNull
    @DecimalMin("0.01")
    BigDecimal value;

    @Min(0)
    int amount;

    @NotEmpty
    @Size(min = 3, max = 99)
    @Valid
    List<ProductDetailRequest> details;

    @NotBlank
    @Length(min = 1, max = 1000)
    String description;

    @JsonProperty("id_category")
    @ExistsId(domainClass = Category.class)
    Long idCategory;

    @JsonIgnore
    User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Product toModel(EntityManager manager) {

        Category category = manager.find(Category.class, idCategory);
        Assert.isTrue(category != null, "Categoria fornecida não encontrada: " + idCategory);
        Assert.isTrue(owner != null, "Impossível salvar produto sem um Doto");

        List<ProductDetail> productDetails = this.details.stream().map(ProductDetailRequest::toModel)
                .collect(Collectors.toList());

        return new Product(name, value, amount, description, category, owner, productDetails);
    }

    public ProductRequest(@NotBlank String name, @NotNull @DecimalMin("0.01") BigDecimal value, @Min(0) int amount, @NotEmpty @Size(min = 3, max = 99) List<ProductDetailRequest> details, @NotBlank @Length(min = 1, max = 1000) String description, Long idCategory) {
        this.name = name;
        this.value = value;
        this.amount = amount;
        this.details = details;
        this.description = description;
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }

    public List<ProductDetailRequest> getDetails() {
        return details;
    }

    public String getDescription() {
        return description;
    }

    public Long getIdCategory() {
        return idCategory;
    }
}
