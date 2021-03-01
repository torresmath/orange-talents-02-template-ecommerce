package com.zup.mercadolivre.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.builders.MockMvcBuilder;
import com.zup.mercadolivre.category.controller.request.CategoryRequest;
import com.zup.mercadolivre.category.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CategoryControllerTest {

    static final String DEFAULT_CATEGORY = "categoria-padrao";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    MessageSource messageSource;


    @Test
    @DisplayName("Deveria devolver 403")
    void deveriaDevolver403() throws Exception {

        CategoryRequest content = new CategoryRequest("categoria", null);
        ResultActions resultActions = new MockMvcBuilder().perform("/category", content, 403, mapper, mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria passar com id parent nulo")
    void deveriaPassarSemIdParent() throws Exception {

        CategoryRequest content = new CategoryRequest("categoria", null);
        ResultActions resultActions = new MockMvcBuilder().perform("/category", content, 200, mapper, mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria passar com id parent válido")
    void deveriaPassarComIdParent() throws Exception {

        CategoryRequest content = new CategoryRequest("categoria", 1L);
        ResultActions resultActions = new MockMvcBuilder().perform("/category", content, 200, mapper, mockMvc);

        List<Category> query = em.createQuery("select c from Category c", Category.class).getResultList();
        Category category = query.get(1);

        assertAll(
                () -> assertEquals(2, query.size()),
                () -> assertEquals(content.getName(), category.getName()),
                () -> assertNotNull(category.getParentCategory()),
                () -> assertEquals(content.getIdParent(), category.getParentCategory().getId())
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 com id parent inválido")
    void deveriaDevolver400ComIdParentNotFound() throws Exception {

        CategoryRequest content = new CategoryRequest("categoria", 2L);
        ResultActions resultActions = new MockMvcBuilder().perform("/category", content, 400, mapper, mockMvc);

        List<Category> query = em.createQuery("select c from Category c", Category.class).getResultList();
        Category category = query.get(0);

        assertAll(
                () -> assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains(messageSource.getMessage("category.not_found", null, LocaleContextHolder.getLocale()))),
                () -> assertEquals(1, query.size()),
                () -> assertEquals(DEFAULT_CATEGORY, category.getName()),
                () -> assertNull(category.getParentCategory())
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 com nome dupicado")
    void deveriaDevolver400ComNomeDuplicado() throws Exception {

        CategoryRequest content = new CategoryRequest(DEFAULT_CATEGORY, null);
        ResultActions resultActions = new MockMvcBuilder().perform("/category", content, 400, mapper, mockMvc);

        List<Category> query = em.createQuery("select c from Category c", Category.class).getResultList();
        Category category = query.get(0);

        assertAll(
                () -> assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("_duplicated_field")),
                () -> assertEquals(1, query.size()),
                () -> assertEquals(DEFAULT_CATEGORY, category.getName()),
                () -> assertNull(category.getParentCategory())
        );
    }
}