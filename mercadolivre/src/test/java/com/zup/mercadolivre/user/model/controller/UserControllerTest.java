package com.zup.mercadolivre.user.model.controller;

import com.zup.mercadolivre.builders.JsonDataBuilder;
import com.zup.mercadolivre.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import java.net.URI;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private EntityManager em;

    @BeforeEach
    public void entityManager() {
        this.em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void rollback() {
        em.getTransaction().rollback();
    }

    @Test
    void deveriaDevolver200() throws Exception {

        URI uri = new URI("/user");

        String json = new JsonDataBuilder()
                .keyValue("login", "email@email.com")
                .keyValue("password", "123456")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(200));
    }

    @Test
    void deveriaDevolver400CasoLoginNaoSejaEmail() throws Exception {

        URI uri = new URI("/user");

        String json = new JsonDataBuilder()
                .keyValue("login", "emailemail.com")
                .keyValue("password", "123456")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(400));

        Assertions.assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("must be a well-formed email address"));
    }

    @Test
    void deveriaDevolver400CasoSenhaSejaMenorQueSeisCaracteres() throws Exception {
        URI uri = new URI("/user");

        String json = new JsonDataBuilder()
                .keyValue("login", "email@email.com")
                .keyValue("password", "12345")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        );

        Assertions.assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("length must be between 6 and 40"));
    }

}