package com.zup.mercadolivre.category.controller;

import com.zup.mercadolivre.category.controller.dto.CategoryDto;
import com.zup.mercadolivre.category.controller.request.CategoryRequest;
import com.zup.mercadolivre.category.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @PersistenceContext
    EntityManager manager;

    @Autowired
    private ParentCategoryNullOrExistsValidator parentCategoryNullOrExistsValidator;

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(parentCategoryNullOrExistsValidator);
    }

    @GetMapping
    public List<CategoryDto> get() {

        return manager.createQuery("select c from Category c", Category.class).getResultList()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        manager.persist(categoryRequest.toModel(manager));
        return ResponseEntity.ok().build();
    }
}
