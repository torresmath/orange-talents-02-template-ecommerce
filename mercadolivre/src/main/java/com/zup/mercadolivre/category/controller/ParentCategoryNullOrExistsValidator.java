package com.zup.mercadolivre.category.controller;

import com.zup.mercadolivre.category.controller.request.CategoryRequest;
import com.zup.mercadolivre.category.model.Category;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class ParentCategoryNullOrExistsValidator implements Validator {

    @PersistenceContext
    EntityManager manager;

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        CategoryRequest categoryRequest = (CategoryRequest) o;

        Long idParent = categoryRequest.getIdParent();

        if (idParent == null) {
            return;
        }

        Category category = manager.find(Category.class, idParent);

        if (category == null) {
            errors.rejectValue("idParent", "category.not_found");
        }

    }
}
