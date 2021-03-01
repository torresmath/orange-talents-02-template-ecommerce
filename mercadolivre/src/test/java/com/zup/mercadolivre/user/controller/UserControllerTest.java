package com.zup.mercadolivre.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.user.controller.request.UserRequest;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerTest {

    static final String DEFAULT_LOGIN = "email@emuso.com";
    static final String DEFAULT_PASSWORD = "senhaemuso";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deveria devolver 200 com informações válidas")
    void deveriaDevolver200() throws Exception {

        UserRequest userRequest = new UserRequest("email@email.com", "123456");

        new MockMvcBuilder().perform("/user", userRequest, 200, mapper, mockMvc);

        List<User> query = em.createQuery("select u from User u", User.class).getResultList();
        User user = query.get(2);
        assertAll(
                () -> assertNotNull(query),
                () -> assertEquals(3, query.size()),
                () -> assertEquals(userRequest.getLogin(), user.getLogin())
        );
    }

    @Test
    @DisplayName("Deveria devolver 400 caso login esteja mal formatado")
    void deveriaDevolver400CasoLoginNaoSejaEmail() throws Exception {

        UserRequest userRequest = new UserRequest("emailemail.com", "123456");

        ResultActions resultActions = new MockMvcBuilder().perform("/user", userRequest, 400, mapper, mockMvc);

        List<User> query = em.createQuery("select u from User u", User.class).getResultList();
        User user = query.get(0);
        assertAll(
                () -> assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("must be a well-formed email address")),
                () -> assertNotNull(query),
                () -> assertEquals(2, query.size()),
                () -> assertNotEquals(userRequest.getLogin(), user.getLogin()),
                () -> assertEquals(DEFAULT_LOGIN, user.getLogin())
        );
    }

    @Test
    @DisplayName("Deveria devolver 400 caso senha seja menor que 6 caracteres")
    void deveriaDevolver400CasoSenhaSejaMenorQueSeisCaracteres() throws Exception {

        UserRequest userRequest = new UserRequest("email@email.com", "12345");

        ResultActions resultActions = new MockMvcBuilder().perform("/user", userRequest, 400, mapper, mockMvc);

        List<User> query = em.createQuery("select u from User u", User.class).getResultList();
        User user = query.get(0);
        assertAll(
                () -> assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("length must be between 6 and 40")),
                () -> assertNotNull(query),
                () -> assertEquals(2, query.size()),
                () -> assertNotEquals(userRequest.getLogin(), user.getLogin()),
                () -> assertEquals(DEFAULT_LOGIN, user.getLogin())
        );
    }

    @Test
    @DisplayName("Deveria devolver 400 caso login não seja único")
    void deveriaDevolver400CasoLoginJaExista() throws Exception {

        UserRequest userRequest = new UserRequest(DEFAULT_LOGIN, "123456");

        ResultActions resultActions = new MockMvcBuilder().perform("/user", userRequest, 400, mapper, mockMvc);

        List<User> query = em.createQuery("select u from User u", User.class).getResultList();
        User user = query.get(0);
        assertAll(
                () -> assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("_duplicated_field")),
                () -> assertNotNull(query),
                () -> assertEquals(2, query.size()),
                () -> assertEquals(DEFAULT_PASSWORD, user.getPassword())
        );
    }

}