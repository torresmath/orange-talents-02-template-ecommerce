package com.zup.mercadolivre.product.controller.request;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRequestTest {

    @Mock
    EntityManager manager;

    @Test
    @DisplayName("Deveria devolver Exception se categoria não for encontrada")
    void deveriaDevolverExceptionCategoria() {

        ProductRequest productRequest = new ProductRequest("nome", BigDecimal.ONE, 1, Collections.emptyList(), "desc", 100L);

        when(manager.find(Category.class, 100L))
                .thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> productRequest.toModel(manager));
    }

    @Test
    @DisplayName("Deveria devolver Exception se owner não tiver sido mapeado")
    void deveriaDevolverExceptionOwner() {

        ProductRequest productRequest = new ProductRequest("nome", BigDecimal.ONE, 1, Collections.emptyList(), "desc", 100L);

        when(manager.find(Category.class, 100L))
                .thenReturn(new Category());

        Assertions.assertThrows(IllegalArgumentException.class, () -> productRequest.toModel(manager));
    }

    @Test
    @DisplayName("Deveria devolver objeto")
    void deveriaDevolverObjeto() {

        ProductRequest productRequest = new ProductRequest("nome", BigDecimal.ONE, 1, Collections.emptyList(), "desc", 100L);

        productRequest.setOwner(new User());

        when(manager.find(Category.class, 100L))
                .thenReturn(new Category());


        Product product = productRequest.toModel(manager);
        Assertions.assertNotNull(product);
    }

}