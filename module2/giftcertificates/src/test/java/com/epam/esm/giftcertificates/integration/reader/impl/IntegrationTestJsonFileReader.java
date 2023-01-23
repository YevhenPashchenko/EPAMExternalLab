package com.epam.esm.giftcertificates.integration.reader.impl;

import com.epam.esm.giftcertificates.integration.reader.IntegrationTestFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntegrationTestJsonFileReader implements IntegrationTestFileReader<JsonNode> {

    @Autowired
    private final ObjectMapper objectMapper;

    public IntegrationTestJsonFileReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode read(String path) throws IOException {
        try(var inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return objectMapper.readValue(inputStream, JsonNode.class);
        }
    }
}
