package com.zup.mercadolivre.product.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @ManyToOne
    Product product;

    public ProductDetail(@NotBlank String name, @NotBlank String description, Product product) {
        this.name = name;
        this.description = description;
        this.product = product;
    }

    @Deprecated
    public ProductDetail() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
