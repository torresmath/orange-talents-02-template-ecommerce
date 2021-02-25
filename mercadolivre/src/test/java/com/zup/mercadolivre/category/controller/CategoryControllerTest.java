package com.zup.mercadolivre.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.category.controller.request.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static com.zup.mercadolivre.builders.MockMvcBuilder.perform;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CategoryControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {

//        CategoryRequest content = new CategoryRequest("categoria", null);
//        ResultActions resultActions = perform("/category", content, 200, mapper, mockMvc);
    }
}