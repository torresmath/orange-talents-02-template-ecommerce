package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.controller.request.PurchaseRequest;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurcharseProductAndAmountValidatorTest {

    private static final String ID_PARENT = "idProduct";

    @Mock
    private EntityManager manager;

    @Mock
    private Errors errors;

    @Autowired
    @InjectMocks
    private PurcharseProductAndAmountValidator validator;

    private Product validProduct;
    private Product invalidProduct;

    @BeforeEach
    public void init() {
        validProduct = new Product("P", BigDecimal.ONE, 5, "D", new Category(), new User());
        invalidProduct = new Product("P", BigDecimal.ONE, 0, "D", new Category(), new User());
    }

    @Test
    @DisplayName("Deveria passar com request válido In Point")
    void deveriaPassar() {

        when(manager.find(Product.class, 1L))
                .thenReturn(validProduct);

        PurchaseRequest request = new PurchaseRequest(1L, 5, "PAG_SEGURO");

        validator.validate(request, errors);

        assertAll(
                () -> assertFalse(errors.hasErrors()),
                () -> verify(manager, times(1)).find(Product.class, 1L)
        );
    }

    @Test
    @DisplayName("Deveria abordar validação caso já existam erros")
    void deveriaAbordarComError() {

        when(errors.hasErrors())
                .thenReturn(true);

        PurchaseRequest request = new PurchaseRequest(1L, 1, "PAG_SEGURO");

        validator.validate(request, errors);

        assertAll(
                () -> assertTrue(errors.hasErrors()),
                () -> verifyNoInteractions(manager)
        );
    }

    @Test
    @DisplayName("Deveria validar produto nulo")
    void deveriaValidarProdutoNulo() {

        when(errors.hasErrors())
                .thenReturn(false);

        when(manager.find(Product.class, 1L))
                .thenReturn(null);

        PurchaseRequest request = new PurchaseRequest(1L, 1, "PAG_SEGURO");

        validator.validate(request, errors);

        assertAll(
                () -> verify(errors, times(1)).rejectValue(ID_PARENT, "product.not_found"),
                () -> verify(manager, times(1)).find(Product.class, 1L)
        );
    }

    @Test
    @DisplayName("Deveria validar produto Sem estoque")
    void deveriaValidarProdutoSemEstoque() {

        when(errors.hasErrors())
                .thenReturn(false);

        when(manager.find(Product.class, 1L))
                .thenReturn(invalidProduct);

        PurchaseRequest request = new PurchaseRequest(1L, 1, "PAG_SEGURO");

        validator.validate(request, errors);

        assertAll(
                () -> verify(errors, times(1)).rejectValue(ID_PARENT, "product.out_of_stock"),
                () -> verify(manager, times(1)).find(Product.class, 1L)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 999})
    @DisplayName("Deveria validar Quantidade superior Off Point")
    void deveriaValidarQuantidadeSuperiorOffPoint(int amount) {

        when(errors.hasErrors())
                .thenReturn(false);

        when(manager.find(Product.class, 1L))
                .thenReturn(validProduct);

        PurchaseRequest request = new PurchaseRequest(1L, amount, "PAG_SEGURO");

        validator.validate(request, errors);

        assertAll(
                () -> verify(errors, times(1)).rejectValue(ID_PARENT, "product.insufficient_amount"),
                () -> verify(manager, times(1)).find(Product.class, 1L)
        );
    }

}