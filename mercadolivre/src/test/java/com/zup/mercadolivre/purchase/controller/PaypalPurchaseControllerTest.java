package com.zup.mercadolivre.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.purchase.controller.request.PaypalTransactionRequest;
import com.zup.mercadolivre.purchase.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class PaypalPurchaseControllerTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 200")
    void deveriaRetornar200() throws Exception {
        PaypalTransactionRequest request = new PaypalTransactionRequest("123456", "identifier-padrao", 1);

        new MockMvcBuilder().perform("/api/v1/paypal", request, 200, mapper, mockMvc);

        List<Transaction> transactions = manager.createQuery("select p from Transaction p", Transaction.class).getResultList();

        assertEquals(1, transactions.size());
    }

    @ParameterizedTest
    @WithMockUser
    @DisplayName("Deveria retornar 400 codigo nao mapeado")
    @ValueSource(ints = {-1, 2, 3, 9999, -9999})
    void deveriaRetornar400CodigoNaoMapeado(int status) throws Exception {
        PaypalTransactionRequest request = new PaypalTransactionRequest("123456", "identifier-padrao", status);

        new MockMvcBuilder().perform("/api/v1/paypal", request, 400, mapper, mockMvc);

        List<Transaction> transactions = manager.createQuery("select p from Transaction p", Transaction.class).getResultList();

        assertAll(
                () -> assertTrue(transactions.isEmpty())
        );


    }
}