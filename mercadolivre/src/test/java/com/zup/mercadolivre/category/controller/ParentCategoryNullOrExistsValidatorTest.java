package com.zup.mercadolivre.category.controller;

import com.zup.mercadolivre.category.controller.request.CategoryRequest;
import com.zup.mercadolivre.category.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentCategoryNullOrExistsValidatorTest {

    static final String DEFAULT_CATEGORY = "categoria-padrao";

    @Mock
    EntityManager manager;

    @Mock
    Errors errors;

    @Autowired
    @InjectMocks
    ParentCategoryNullOrExistsValidator validator;

    @Test
    @DisplayName("Deveria passar caso Id Parent seja nulo")
    void deveriaPassarComIdNulo() {

        CategoryRequest category = new CategoryRequest("nova categoria", null);

        validator.validate(category, errors);

        assertAll(
                () -> assertFalse(errors.hasErrors()),
                () -> verifyNoInteractions(manager)
        );
    }

    @Test
    @DisplayName("Deveria passar caso Id Parent seja válido")
    void deveriaPassarComIdValido() {

        when(manager.find(Category.class, 1L))
                .thenReturn(new Category(DEFAULT_CATEGORY, null));

        CategoryRequest category = new CategoryRequest("nova categoria", 1L);

        validator.validate(category, errors);

        assertAll(
                () -> verify(manager, times(1)).find(Category.class, 1L),
                () -> verify(errors, times(0)).rejectValue("idParent", "category.not_found"),
                () -> verify(errors, times(0)).rejectValue(anyString(), anyString())
        );
    }

    @Test
    @DisplayName("Deveria abortar validação caso já existam erros")
    void deveriaAbortarComErrors() {

        CategoryRequest category = new CategoryRequest("nova categoria", null);

        when(errors.hasErrors()).thenReturn(true);

        validator.validate(category, errors);

        assertAll(
                () -> assertTrue(errors.hasErrors()),
                () -> verifyNoInteractions(manager)
        );
    }

    @Test
    @DisplayName("Deveria validar caso id parent não exista")
    void deveriaValidarComIdInvalido() {

        when(manager.find(Category.class, 1L))
                .thenReturn(null);

        CategoryRequest category = new CategoryRequest("nova categoria", 1L);

        validator.validate(category, errors);

        assertAll(
                () -> verify(manager, times(1)).find(Category.class, 1L),
                () -> verify(errors, times(1)).rejectValue("idParent", "category.not_found"),
                () -> verify(errors, times(1)).rejectValue(anyString(), anyString())
        );
    }
}