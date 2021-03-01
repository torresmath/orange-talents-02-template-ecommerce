package com.zup.mercadolivre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.product.model.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        byte[] mockContent = new byte[0];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", "url-test", "", mockContent);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/product/1/image")
                        .file(mockMultipartFile)
        ).andExpect(status().is(200));

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

        byte[] mockContent = new byte[0];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", "url-test", "", mockContent);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("images", "url-test", "", mockContent);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("images", "url-test", "", mockContent);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/product/1/image")
                        .file(mockMultipartFile)
                        .file(mockMultipartFile1)
                        .file(mockMultipartFile2)
        ).andExpect(status().is(200));

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

        byte[] mockContent = new byte[0];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", "url-test", "", mockContent);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/product/999/image")
                        .file(mockMultipartFile)
        ).andExpect(status().is(400));

    }

    @Test
    @WithUserDetails("email@secundario.com")
    @DisplayName("Deveria retornar 403")
    void deveriaRetornar403() throws Exception {

        byte[] mockContent = new byte[0];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", "url-test", "", mockContent);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/product/1/image")
                        .file(mockMultipartFile)
        ).andExpect(status().is(403));
    }

}