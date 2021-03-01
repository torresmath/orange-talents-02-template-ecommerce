package com.zup.mercadolivre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.product.controller.request.ProductQuestionRequest;
import com.zup.mercadolivre.product.model.ProductQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ProductQuestionControllerTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("email@emuso.com")
    void deveriaRetornar200() throws Exception {

        ProductQuestionRequest request = new ProductQuestionRequest("title");

        new MockMvcBuilder().perform("/product/1/question", request, 200, mapper, mockMvc);

        List<ProductQuestion> questions = manager.createQuery("select q from ProductQuestion q", ProductQuestion.class).getResultList();

        ProductQuestion question = questions.get(1);
        assertAll(
                () -> assertEquals(2, questions.size()),
                () -> assertEquals(question.getTitle(), request.getTitle())
        );
    }

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 404")
    void deveriaRetornar404() throws Exception {
        ProductQuestionRequest request = new ProductQuestionRequest("title");

        new MockMvcBuilder().perform("/product/999/question", request, 404, mapper, mockMvc);

        List<ProductQuestion> questions = manager.createQuery("select q from ProductQuestion q", ProductQuestion.class).getResultList();

        assertAll(
                () -> assertEquals(1, questions.size())
        );
    }
}