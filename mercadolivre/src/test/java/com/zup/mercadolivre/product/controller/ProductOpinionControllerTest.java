package com.zup.mercadolivre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.product.controller.request.ProductOpinionRequest;
import com.zup.mercadolivre.product.model.ProductOpinion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ProductOpinionControllerTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private String maxDescription = "";

    @BeforeEach
    public void initialize() {

        for (int i = 0; i < 500; i++) {
            maxDescription += "d";
        }
    }

    @ParameterizedTest
    @DisplayName("Deveria retornar 200 com Rating válido")
    @WithUserDetails("email@emuso.com")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void deveriaRetornar200(int rating) throws Exception {

        ProductOpinionRequest request = new ProductOpinionRequest(rating, "Cool", "A description");

        new MockMvcBuilder().perform("/product/1/opinion", request, 200, mapper, mockMvc);

        List<ProductOpinion> opinions = manager.createQuery("select o from ProductOpinion o", ProductOpinion.class).getResultList();

        ProductOpinion opinion = opinions.get(0);
        assertAll(
                () -> assertEquals(1, opinions.size()),
                () -> assertEquals(request.getRating(), opinion.getRating()),
                () -> assertEquals(request.getDescription(), opinion.getDescription()),
                () -> assertEquals(request.getTitle(), opinion.getTitle())
        );
    }

    @ParameterizedTest
    @DisplayName("Deveria retornar 400 com Rating menor que o permitido")
    @WithUserDetails("email@emuso.com")
    @ValueSource(ints = {-1, 0, Integer.MIN_VALUE})
    void deveriaRetornar400Rating(int rating) throws Exception {

        ProductOpinionRequest request = new ProductOpinionRequest(rating, "Cool", maxDescription);

        ResultActions perform = new MockMvcBuilder().perform("/product/1/opinion", request, 400, mapper, mockMvc);

        List<ProductOpinion> opinions = manager.createQuery("select o from ProductOpinion o", ProductOpinion.class).getResultList();

        String response = perform.andReturn().getResponse().getContentAsString();
        assertAll(
                () -> assertEquals(0, opinions.size()),
                () -> assertTrue(response.contains("must be greater than or equal to 1"))
        );
    }

    @ParameterizedTest
    @DisplayName("Deveria retornar 400 com Rating maior que o permitido")
    @WithUserDetails("email@emuso.com")
    @ValueSource(ints = {6, 7, Integer.MAX_VALUE})
    void deveriaRetornar400RatingMaior(int rating) throws Exception {

        ProductOpinionRequest request = new ProductOpinionRequest(rating, "Cool", "A description");

        ResultActions perform = new MockMvcBuilder().perform("/product/1/opinion", request, 400, mapper, mockMvc);

        List<ProductOpinion> opinions = manager.createQuery("select o from ProductOpinion o", ProductOpinion.class).getResultList();

        String response = perform.andReturn().getResponse().getContentAsString();
        assertAll(
                () -> assertEquals(0, opinions.size()),
                () -> assertTrue(response.contains("must be less than or equal to 5"))
        );
    }

    @Test
    @DisplayName("Deveria retornar 400 com descrição Off Point")
    @WithUserDetails("email@emuso.com")
    void deveriaRetornar400RatingMaior() throws Exception {

        ProductOpinionRequest request = new ProductOpinionRequest(3, "Cool", maxDescription + "a");

        ResultActions perform = new MockMvcBuilder().perform("/product/1/opinion", request, 400, mapper, mockMvc);

        List<ProductOpinion> opinions = manager.createQuery("select o from ProductOpinion o", ProductOpinion.class).getResultList();

        String response = perform.andReturn().getResponse().getContentAsString();
        assertAll(
                () -> assertEquals(0, opinions.size()),
                () -> assertTrue(response.contains("size must be between 1 and 500"))
        );
    }

    @Test
    @DisplayName("Deveria retornar 404")
    @WithUserDetails("email@emuso.com")
    void deveriaRetornar404() throws Exception {

        ProductOpinionRequest request = new ProductOpinionRequest(3, "Cool", maxDescription);

        ResultActions perform = new MockMvcBuilder().perform("/product/2/opinion", request, 404, mapper, mockMvc);

        List<ProductOpinion> opinions = manager.createQuery("select o from ProductOpinion o", ProductOpinion.class).getResultList();

        assertAll(
                () -> assertEquals(0, opinions.size())
        );
    }
}