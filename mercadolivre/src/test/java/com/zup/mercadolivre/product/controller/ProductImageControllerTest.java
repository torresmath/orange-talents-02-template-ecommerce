package com.zup.mercadolivre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.product.controller.request.ProductImageRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.zup.mercadolivre.builders.MockMvcBuilder.perform;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ProductImageControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager manager;

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 200")
    void deveriaRetornar200() throws Exception {

        ProductImageRequest request = new ProductImageRequest(Collections.singletonList("url-test"));
        Product product = manager.find(Product.class, 1L);
        perform("/product/1/image", request, 200, mapper, mockMvc);
        
        List<ProductImage> query = manager.createQuery("select i from ProductImage i", ProductImage.class).getResultList();
        ProductImage productImage = query.get(0);
        assertAll(
                () -> assertEquals(1, query.size()),
                () -> assertEquals("dev-url-test", productImage.getLink())
        );
    }

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 200 com Multiplas imagens")
    void deveriaRetornar200Multiplos() throws Exception {

        ProductImageRequest request = new ProductImageRequest(Arrays.asList("url-test", "url-test", "url-test"));
        Product product = manager.find(Product.class, 1L);
        perform("/product/1/image", request, 200, mapper, mockMvc);

        List<ProductImage> query = manager.createQuery("select i from ProductImage i", ProductImage.class).getResultList();

        query.forEach(img -> assertEquals("dev-url-test", img.getLink()));
        assertAll(
                () -> assertEquals(3, query.size())
        );
    }

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 400")
    void deveriaRetornar400() throws Exception {

        ProductImageRequest request = new ProductImageRequest(Collections.singletonList("url-test"));

        perform("/product/999/image", request, 400, mapper, mockMvc);
    }

    @Test
    @WithUserDetails("email@secundario.com")
    @DisplayName("Deveria retornar 403")
    void deveriaRetornar403() throws Exception {

        ProductImageRequest request = new ProductImageRequest(Collections.singletonList("url-test"));

        perform("/product/1/image", request, 403, mapper, mockMvc);
    }

}