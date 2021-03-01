package com.zup.mercadolivre.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.product.controller.request.ProductDetailRequest;
import com.zup.mercadolivre.product.controller.request.ProductRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    ProductRequest productRequest;
    List<ProductDetailRequest> productDetailRequests;

    @BeforeEach
    public void initialize() {

        productDetailRequests = Arrays.asList(
                new ProductDetailRequest("detail1", "desc1"),
                new ProductDetailRequest("detail2", "desc2"),
                new ProductDetailRequest("detail3", "desc3")
        );

        productRequest = new ProductRequest(
                "produto",
                BigDecimal.valueOf(10),
                1,
                productDetailRequests,
                "description",
                1L
        );
    }

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 200")
    void testDeveriaRetornar200() throws Exception {

        new MockMvcBuilder().perform("/product", this.productRequest, 200, mapper, mockMvc);

        List<Product> products = em.createQuery("select p from Product p", Product.class).getResultList();
        List<ProductDetail> productDetails = em.createQuery("select p from ProductDetail p", ProductDetail.class).getResultList();

        assertAll(
                () -> assertEquals(2, products.size()),
                () -> assertEquals(3, productDetails.size())
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 400 com lista de detalhes vazia ou menor que 3")
    void testDeveriaRetornar400ListaVazia() throws Exception {

        ProductRequest productRequest = new ProductRequest(
                "produto",
                BigDecimal.valueOf(10),
                1,
                Collections.emptyList(),
                "description",
                1L
        );

        ResultActions resultActions = new MockMvcBuilder().perform("/product", productRequest, 400, mapper, mockMvc);

        String response = resultActions.andReturn().getResponse().getContentAsString();

        List<Product> query = em.createQuery("select p from Product p", Product.class).getResultList();
        assertAll(
                () -> assertTrue(response.contains("size must be between 3 and 99")),
                () -> assertTrue(response.contains("must not be empty")),
                () -> assertEquals(1, query.size())
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 400 com id category invalido")
    void testDeveriaRetornar400CategoriaNaoEncontrada() throws Exception {

        ProductRequest productRequest = new ProductRequest(
                "produto",
                BigDecimal.valueOf(10),
                1,
                Collections.emptyList(),
                "description",
                999L
        );

        ResultActions resultActions = new MockMvcBuilder().perform("/product", productRequest, 400, mapper, mockMvc);

        String response = resultActions.andReturn().getResponse().getContentAsString();

        List<Product> query = em.createQuery("select p from Product p", Product.class).getResultList();
        assertAll(
                () -> assertTrue(response.contains("size must be between 3 and 99")),
                () -> assertEquals(1, query.size())
        );
    }

    @Test
    @WithMockUser(username = "email@emuso.com", password = "senhaemuso")
    @DisplayName("Deveria retornar 400 com value inválido")
    void testDeveriaRetornar400ValorInvalido() throws Exception {

        ProductRequest productRequest = new ProductRequest(
                "produto",
                BigDecimal.ZERO.stripTrailingZeros(),
                1,
                productDetailRequests,
                "description",
                1L
        );

        ResultActions resultActions = new MockMvcBuilder().perform("/product", productRequest, 400, mapper, mockMvc);

        String response = resultActions.andReturn().getResponse().getContentAsString();

        List<Product> query = em.createQuery("select p from Product p", Product.class).getResultList();
        assertAll(
                () -> assertTrue(response.contains("must be greater than or equal to 0.01")),
                () -> assertEquals(1, query.size())
        );
    }

    @Test
    @DisplayName("Deveria retornar 200 com value In point")
    @WithUserDetails("email@emuso.com")
    void testDeveriaRetornar200ComValorInPoint() throws Exception {

        ProductRequest productRequest = new ProductRequest(
                "produto",
                BigDecimal.valueOf(0.01),
                1,
                productDetailRequests,
                "description",
                1L
        );

        ResultActions resultActions = new MockMvcBuilder().perform("/product", productRequest, 200, mapper, mockMvc);

        List<Product> query = em.createQuery("select p from Product p", Product.class).getResultList();
        assertAll(
                () -> assertEquals( 2, query.size())
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 400 com value Off point")
    void testDeveriaRetornar400ComValorOffPoint() throws Exception {

        ProductRequest productRequest = new ProductRequest(
                "produto",
                BigDecimal.valueOf(0.009),
                1,
                productDetailRequests,
                "description",
                1L
        );

        ResultActions resultActions = new MockMvcBuilder().perform("/product", productRequest, 400, mapper, mockMvc);

        String response = resultActions.andReturn().getResponse().getContentAsString();

        List<Product> query = em.createQuery("select p from Product p", Product.class).getResultList();
        assertAll(
                () -> assertTrue(response.contains("must be greater than or equal to 0.01")),
                () -> assertEquals(1, query.size())
        );
    }

}