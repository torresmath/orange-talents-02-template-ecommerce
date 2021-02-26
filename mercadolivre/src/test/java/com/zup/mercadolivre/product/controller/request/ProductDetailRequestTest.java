package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductDetailRequestTest {


    @Test
    @DisplayName("Deveria retornar exception caso produto seja nulo")
    void deveriaRetornarExceptionCasoProdutoSejaNulo() {

        ProductDetailRequest productDetailRequest = new ProductDetailRequest("nome", "description");
        Assertions.assertThrows(IllegalArgumentException.class, () -> productDetailRequest.toModel(null));
    }

    @Test
    @DisplayName("Deveria retornar objeto válido")
    void deveriaRetornarObjeto() {

        Product product = new Product("nome", BigDecimal.TEN, 1, "Description", new Category("name", null), new User());
        ProductDetailRequest productDetailRequest = new ProductDetailRequest("nome", "description");
        ProductDetail detail = productDetailRequest.toModel(product);
        Assertions.assertNotNull(detail);
    }
}