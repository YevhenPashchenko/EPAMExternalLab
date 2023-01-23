package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.config.SpringConfig;
import com.epam.esm.giftcertificates.integration.config.IntegrationTestSpringConfig;
import com.epam.esm.giftcertificates.integration.container.IntegrationTestPostgreSqlContainer;
import com.epam.esm.giftcertificates.integration.initializer.PostgreSqlDataSourceUrlInitializer;
import com.epam.esm.giftcertificates.integration.reader.IntegrationTestFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.esm.giftcertificates.integration.constant.IntegrationTestConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(initializers = PostgreSqlDataSourceUrlInitializer.class,
        classes = {IntegrationTestPostgreSqlContainer.class, SpringConfig.class, IntegrationTestSpringConfig.class})
public class TagControllerIntegrationTest {

    public static GenericContainer<?> postgresSqlContainer = IntegrationTestPostgreSqlContainer.getInstance();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IntegrationTestFileReader<JsonNode> integrationTestFileReader;
    private MockMvc mockMvc;

    @BeforeAll
    public static void start() {
        postgresSqlContainer.start();
    }

    @BeforeEach
    public void setup() throws SQLException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        clearDatabase(this.webApplicationContext);
    }

    @Test
    void shouldReturn200_whenCreateNewTag() throws Exception {
        //GIVEN
        var tagJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TAG_JSON);
        var tagJsonAsString = new ObjectMapper().writeValueAsString(tagJson);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_TAG_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJsonAsString))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCreatedTag_whenGetListOfTags() throws Exception {
        //GIVEN
        var tagJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TAG_JSON);
        var tagJsonAsString = new ObjectMapper().writeValueAsString(tagJson);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_TAG_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJsonAsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_TAGS_URL))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get(0);
        assertEquals(tagJson.get(IntegrationTestConstant.TAG_FIELD_NAME_KEY),
                jsonNode.get(IntegrationTestConstant.TAG_FIELD_NAME_KEY));
    }

    @Test
    void shouldReturnCreatedTag_whenGetTagByIdThatExists() throws Exception {
        //GIVEN
        var tagJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TAG_JSON);
        var tagJsonAsString = new ObjectMapper().writeValueAsString(tagJson);

        //WHEN
        var id = getCreatedTagId(tagJsonAsString);
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_TAG_BY_ID_URL, id))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString());
        assertEquals(tagJson.get(IntegrationTestConstant.TAG_FIELD_NAME_KEY),
                jsonNode.get(IntegrationTestConstant.TAG_FIELD_NAME_KEY));
    }

    @Test
    void shouldReturn404_whenGetGiftCertificateByIdThatNotExists() throws Exception {
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_TAG_BY_ID_URL, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteTag() throws Exception {
        //GIVEN
        var tagJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TAG_JSON);
        var tagJsonAsString = new ObjectMapper().writeValueAsString(tagJson);

        //WHEN
        var id = getCreatedTagId(tagJsonAsString);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(IntegrationTestConstant.DELETE_TAG_URL, id))
                .andExpect(status().isOk());
    }

    private void clearDatabase(WebApplicationContext webApplicationContext) throws SQLException {
        try(Connection connection = webApplicationContext.getBean(DataSource.class).getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(IntegrationTestConstant.CLEAR_DATABASE_QUERY);
        }
    }

    private long getCreatedTagId(String jsonNodeAsString) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_TAG_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNodeAsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_TAGS_URL))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get(0)
                .get(IntegrationTestConstant.TAG_FIELD_ID_KEY).asLong();
    }
}
