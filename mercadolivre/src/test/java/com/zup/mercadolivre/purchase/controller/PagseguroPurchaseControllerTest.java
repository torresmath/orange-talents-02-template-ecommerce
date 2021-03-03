package com.zup.mercadolivre.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.purchase.controller.request.PagseguroTransactionRequest;
import com.zup.mercadolivre.purchase.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
class PagseguroPurchaseControllerTest {

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

        PagseguroTransactionRequest request = new PagseguroTransactionRequest("123456", "identifier-padrao", "SUCESSO");

        new MockMvcBuilder().perform("/api/v1/pagseguro", request, 200, mapper, mockMvc);

        List<Transaction> transactions = manager.createQuery("select p from Transaction p", Transaction.class).getResultList();

        assertEquals(1, transactions.size());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 400 Compra nao encontrada")
    void deveriaRetornar400CompraNotFound() throws Exception {

        PagseguroTransactionRequest request = new PagseguroTransactionRequest("123456", "identifier-errado", "SUCESSO");

        ResultActions perform = new MockMvcBuilder().perform("/api/v1/pagseguro", request, 404, mapper, mockMvc);

        System.out.println("perform.andReturn().getResponse().getContentAsString() = " + perform.andReturn().getResponse().getContentAsString());
    }
}