package com.epam.esm.giftcertificates.integration.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode readJsonFile(String path) throws IOException {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return objectMapper.readValue(inputStream, JsonNode.class);
        }
    }
}
