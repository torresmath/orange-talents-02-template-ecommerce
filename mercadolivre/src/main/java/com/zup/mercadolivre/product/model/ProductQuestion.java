package com.zup.mercadolivre.product.model;

import com.zup.mercadolivre.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ProductQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @CreationTimestamp
    @NotNull
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @NotNull
    private User customer;

    @ManyToOne
    @NotNull
    private Product product;

    @Deprecated
    public ProductQuestion() {

    }

    public ProductQuestion(@NotNull @NotBlank String title, @NotNull User customer, @NotNull Product product) {
        this.title = title;
        this.customer = customer;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCustomer() {
        return customer.getLogin();
    }

    public Product getProduct() {
        return product;
    }
}
