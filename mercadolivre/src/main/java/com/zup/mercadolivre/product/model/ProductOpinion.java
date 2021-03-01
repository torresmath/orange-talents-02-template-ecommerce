package com.zup.mercadolivre.product.model;

import com.zup.mercadolivre.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class ProductOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    @NotNull
    private int rating;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 500)
    private String description;

    @ManyToOne
    @NotNull
    private User customer;

    @ManyToOne
    @NotNull
    private Product product;

    public ProductOpinion(@Min(1) @Max(5) @NotNull int rating,
                          @NotNull @NotBlank String title,
                          @NotNull @NotBlank @Size(min = 1, max = 500) String description,
                          @NotNull User customer,
                          @NotNull Product product) {
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.customer = customer;
        this.product = product;
    }

    @Deprecated
    public ProductOpinion() { }

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
