package com.zup.mercadolivre.category.controller.dto;

import com.zup.mercadolivre.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private final Long id;

    private final String name;

    private final List<CategoryDto> childrenCategories;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.childrenCategories = category.getChildrenCategory().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CategoryDto> getChildrenCategories() {
        return childrenCategories;
    }
}
