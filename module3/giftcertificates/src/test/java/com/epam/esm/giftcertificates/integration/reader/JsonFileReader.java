package com.epam.esm.giftcertificates.integration.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonFileReader {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public String readJsonAsString(String path) throws IOException {
    try (var inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
      var json = objectMapper.readValue(inputStream, JsonNode.class);
      return objectMapper.writeValueAsString(json);
    }
  }
}
