package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductOpinion;
import com.zup.mercadolivre.user.model.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.NotThrownAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductOpinionRequestTest {

    @Test
    @DisplayName("Deveria criar modelo")
    void deveriaCriarModelo() {

        ProductOpinionRequest request = new ProductOpinionRequest(3, "title", "description");

        Product product = new Product();
        User user = new User();

        ProductOpinion productOpinion = request.toModel(product, user);

        assertNotNull(productOpinion);
    }

    @Test
    @DisplayName("Deveria retornar Exception Produto Nulo")
    void deveriaRetornarExceptionProdutoNulo() {

        ProductOpinionRequest request = new ProductOpinionRequest(3, "title", "description");

        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> request.toModel(null, user));
    }

    @Test
    @DisplayName("Deveria retornar Exception Usuario Nulo")
    void deveriaRetornarExceptionUsuarioNulo() {

        ProductOpinionRequest request = new ProductOpinionRequest(3, "title", "description");

        Product product = new Product();

        assertThrows(IllegalArgumentException.class, () -> request.toModel(product, null));
    }
}