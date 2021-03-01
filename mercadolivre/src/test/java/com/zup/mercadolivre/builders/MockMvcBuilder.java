package com.zup.mercadolivre.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@ActiveProfiles("test")
public class MockMvcBuilder {

    private MediaType contentType = MediaType.APPLICATION_JSON;

    public ResultActions perform(String uri, Object content, int expectedStatus, ObjectMapper mapper, MockMvc mockMvc) throws Exception {

        String obj = mapper.writeValueAsString(content);

        return mockMvc.perform(
                MockMvcRequestBuilders
                .post(new URI(uri))
                .contentType(contentType)
                .content(obj)
        ).andExpect(MockMvcResultMatchers
        .status()
        .is(expectedStatus));
    }
}
