package com.zup.mercadolivre.product.model;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.user.model.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @Column(columnDefinition = "Decimal(10,2)")
    @DecimalMin("0.01")
    BigDecimal value;

    @Min(0)
    int amount;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 1000)
    String description;

    @ManyToOne
    @NotNull
    Category category;

    @ManyToOne
    @NotNull
    User owner;

    @CreationTimestamp
    private LocalDateTime createDate;

    public Product(@NotNull @NotBlank String name,
                   @NotNull @DecimalMin("0.01") BigDecimal value,
                   @Min(0) int amount,
                   @NotNull @NotBlank @Length(min = 1, max = 1000) String description,
                   @NotNull Category category,
                   @NotNull User owner) {

        this.name = name;
        this.value = value;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    @Deprecated
    public Product() {
    }
}
