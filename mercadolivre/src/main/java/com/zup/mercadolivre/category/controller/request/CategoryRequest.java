package com.zup.mercadolivre.category.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.common.annotations.UniqueValue;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

public class CategoryRequest {

    @NotBlank
    @UniqueValue(domainClass = Category.class, fieldName = "name")
    private final String name;

    @JsonProperty("id_parent")
    @Nullable
    private final Long idParent;

    public CategoryRequest(@NotBlank String name, Long idParent) {
        this.name = name;
        this.idParent = idParent;
    }


    public Category toModel(EntityManager manager) {

        Category parentCategory = Optional.ofNullable(idParent)
                .map(id -> manager.find(Category.class, id))
                .orElse(null);

        return new Category(name, parentCategory);
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Long getIdParent() {
        return idParent;
    }
}
