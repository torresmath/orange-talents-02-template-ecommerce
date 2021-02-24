package com.zup.mercadolivre.builders;

import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile({"test"})
public class JsonDataBuilder {

    private final Map<String, String> body;

    public JsonDataBuilder keyValue(String key, String value) {
        body.put("\"" +key + "\"", "\""+ value + "\"");
        return this;
    }

    public JsonDataBuilder() { body = new HashMap<>(); }

    public String build() {
        return body.toString().replace("=", ":");
    }
}
