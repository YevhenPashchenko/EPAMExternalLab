package com.epam.esm.giftcertificates.integration.config;

import com.epam.esm.giftcertificates.integration.reader.IntegrationTestFileReader;
import com.epam.esm.giftcertificates.integration.reader.impl.IntegrationTestJsonFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm.giftcertificates.integration")
public class IntegrationTestSpringConfig {

    @Bean
    public IntegrationTestFileReader<JsonNode> integrationTestFileReader() {
        return new IntegrationTestJsonFileReader(objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
