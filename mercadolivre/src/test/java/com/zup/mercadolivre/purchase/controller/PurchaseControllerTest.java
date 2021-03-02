package com.zup.mercadolivre.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.controller.request.PurchaseRequest;
import com.zup.mercadolivre.purchase.model.Purchase;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class PurchaseControllerTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 302 com Gateways em qualquer Cameo Case")
    @ValueSource(strings = {"pagseguro", "PAGSEGURO", "pAgSeGuRo", "paypal", "PAYPAL", "pAyPaL"})
    void deveriaRetornar302(String gateway) throws Exception {

        PurchaseRequest request = new PurchaseRequest(1L, 1, gateway);

        new MockMvcBuilder().perform("/purchase", request, 302, mapper, mockMvc);

        List<Purchase> purchases = manager.createQuery("select p from Purchase p", Purchase.class).getResultList();
        Product product = manager.find(Product.class, 1L);

        assertAll(
                () -> assertEquals(2, purchases.size()),
                () -> assertEquals(0, product.getAmount())
        );
    }

    @Test
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 400 Produto nao encontrado")
    void deveriaRetornar400ProdutoNaoEncontrado() throws Exception {

        PurchaseRequest request = new PurchaseRequest(9999L, 1, "PAGSEGURO");

        ResultActions perform = new MockMvcBuilder().perform("/purchase", request, 400, mapper, mockMvc);

        String response = perform.andReturn().getResponse().getContentAsString();
        List<Purchase> purchases = manager.createQuery("select p from Purchase p", Purchase.class).getResultList();

        assertAll(
                () -> assertEquals(1, purchases.size()),
                () -> assertTrue(response.contains("_not_found"))
        );
    }

    @ParameterizedTest
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 400 Quantidade inferior")
    @ValueSource(ints = {0, -1, -9999})
    void deveriaRetornar400QuantidadeMinimoInvalido(int amount) throws Exception {

        PurchaseRequest request = new PurchaseRequest(1L, amount, "PAGSEGURO");

        ResultActions perform = new MockMvcBuilder().perform("/purchase", request, 400, mapper, mockMvc);

        String response = perform.andReturn().getResponse().getContentAsString();
        List<Purchase> purchases = manager.createQuery("select p from Purchase p", Purchase.class).getResultList();

        assertAll(
                () -> assertEquals(1, purchases.size()),
                () -> assertTrue(response.contains("must be greater than or equal to 1"))
        );
    }

    @ParameterizedTest
    @WithUserDetails("email@emuso.com")
    @DisplayName("Deveria retornar 400 Gateway Invalido")
    @ValueSource(strings = {"PAG-SEGURO", "PAG_SEGURO", "PAY_PAL", "pay-pal"})
    void deveriaRetornar400QuantidadeMinimoInvalido(String gateway) throws Exception {

        PurchaseRequest request = new PurchaseRequest(1L, 1, gateway);

        ResultActions perform = new MockMvcBuilder().perform("/purchase", request, 400, mapper, mockMvc);

        String response = perform.andReturn().getResponse().getContentAsString();
        List<Purchase> purchases = manager.createQuery("select p from Purchase p", Purchase.class).getResultList();

        assertAll(
                () -> assertEquals(1, purchases.size()),
                () -> assertTrue(response.contains("_invalid_enum"))
        );
    }
}