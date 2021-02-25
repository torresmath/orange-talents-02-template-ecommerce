package com.zup.mercadolivre.category.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @ManyToOne
    @Nullable
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childrenCategory;

    public Category(@NotNull @NotBlank String name, @Nullable Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }

    @Deprecated
    public Category() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Category getParentCategory() {
        return parentCategory;
    }

    public List<Category> getChildrenCategory() {
        return childrenCategory;
    }
}
