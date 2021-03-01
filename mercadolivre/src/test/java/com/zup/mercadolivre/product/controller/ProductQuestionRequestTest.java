package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductQuestion;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductQuestionRequestTest {

    @Test
    @DisplayName("Deveria criar objeto")
    void deveriaCriarObjeto() {

        ProductQuestionRequest title = new ProductQuestionRequest("title");

        Product product = new Product();
        User user = new User();
        ProductQuestion productQuestion = title.toModel(product, user);

        assertNotNull(productQuestion);
    }

    @Test
    @DisplayName("Deveria retornar exception Produto Nulo")
    void deveriaRetornarExceptionProduto() {

        ProductQuestionRequest title = new ProductQuestionRequest("title");

        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> title.toModel(null, user));
    }

    @Test
    @DisplayName("Deveria retornar exception Usuario Nulo")
    void deveriaRetornarExceptionUsuario() {

        ProductQuestionRequest title = new ProductQuestionRequest("title");

        Product product = new Product();

        assertThrows(IllegalArgumentException.class, () -> title.toModel(product, null));
    }

}