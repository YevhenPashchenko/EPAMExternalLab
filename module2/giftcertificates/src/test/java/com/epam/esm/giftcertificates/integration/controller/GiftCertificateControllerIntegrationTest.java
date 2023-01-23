package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.config.SpringConfig;
import com.epam.esm.giftcertificates.integration.config.IntegrationTestSpringConfig;
import com.epam.esm.giftcertificates.integration.reader.IntegrationTestFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.epam.esm.giftcertificates.integration.constant.IntegrationTestConstant;
import com.epam.esm.giftcertificates.integration.container.IntegrationTestPostgreSqlContainer;
import com.epam.esm.giftcertificates.integration.initializer.PostgreSqlDataSourceUrlInitializer;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(initializers = PostgreSqlDataSourceUrlInitializer.class,
        classes = {IntegrationTestPostgreSqlContainer.class, SpringConfig.class, IntegrationTestSpringConfig.class})
class GiftCertificateControllerIntegrationTest {

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
    void shouldReturn200_whenCreateNewGiftCertificate() throws Exception {
        //GIVEN
        var giftCertificateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateJson);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_GIFT_CERTIFICATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateJsonAsString))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCreatedGiftCertificate_whenGetListOfGiftCertificatesForPage() throws Exception {
        //GIVEN
        var pageNumber = 1;
        var giftCertificateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateJson);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_GIFT_CERTIFICATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateJsonAsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_GIFT_CERTIFICATES_FOR_PAGE_URL, pageNumber))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get(0);
        assertAll(
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY))
        );
    }

    @Test
    void shouldReturnCreatedGiftCertificate_whenGetGiftCertificateByIdThatExists() throws Exception {
        //GIVEN
        var pageNumber = 1;
        var giftCertificateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateJson);

        //WHEN
        var id = getCreatedGiftCertificateId(giftCertificateJsonAsString, pageNumber);
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_GIFT_CERTIFICATE_BY_ID_URL, id))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString());
        assertAll(
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY)),
                () -> assertEquals(giftCertificateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY))
        );
    }

    @Test
    void shouldReturn404_whenGetGiftCertificateByIdThatNotExists() throws Exception {
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_GIFT_CERTIFICATE_BY_ID_URL, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOnlyFirstGiftCertificate_whenGetSortedGiftCertificatesByPartOfName() throws Exception {
        //GIVEN
        var giftCertificateJson1 = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateJson2 = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_SECOND_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateParametersJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TEST_GIFT_CERTIFICATE_PARAMETERS_JSON);
        var giftCertificateJson1AsString = new ObjectMapper().writeValueAsString(giftCertificateJson1);
        var giftCertificateJson2AsString = new ObjectMapper().writeValueAsString(giftCertificateJson2);
        var giftCertificateParametersJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateParametersJson);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_GIFT_CERTIFICATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateJson1AsString))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_GIFT_CERTIFICATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateJson2AsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_GIFT_CERTIFICATES_BY_PARAMETERS_FOR_PAGE_URL, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateParametersJsonAsString))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString());
        assertAll(
                () -> assertEquals(1, jsonNode.size()),
                () -> assertEquals(giftCertificateParametersJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_PARAMETERS_FIELD_PART_NAME_KEY),
                        jsonNode.get(0).get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY))
        );
    }

    @Test
    void shouldUpdateGiftCertificateFields_whenUpdateGiftCertificate() throws Exception {
        //GIVEN
        var pageNumber = 1;
        var giftCertificateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateForUpdateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_TEST_GIFT_CERTIFICATE_FOR_UPDATE_JSON);
        var giftCertificateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateJson);

        //WHEN
        var id = getCreatedGiftCertificateId(giftCertificateJsonAsString, pageNumber);
        ((ObjectNode) giftCertificateForUpdateJson).put(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_ID_KEY, id);
        var giftCertificateForUpdateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateForUpdateJson);
        mockMvc.perform(MockMvcRequestBuilders
                .patch(IntegrationTestConstant.PATCH_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateForUpdateJsonAsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_GIFT_CERTIFICATES_FOR_PAGE_URL, pageNumber))
                .andExpect(status().isOk())
                .andReturn();

        //THEN
        var jsonNode = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get(0);
        assertAll(
                () -> assertEquals(giftCertificateForUpdateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_NAME_KEY)),
                () -> assertEquals(giftCertificateForUpdateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY)),
                () -> assertEquals(giftCertificateForUpdateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_PRICE_KEY)),
                () -> assertEquals(giftCertificateForUpdateJson.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY),
                        jsonNode.get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_DURATION_KEY))
        );
    }

    @Test
    void shouldReturn200_whenDeleteGiftCertificate() throws Exception {
        //GIVEN
        var pageNumber = 1;
        var giftCertificateJson = integrationTestFileReader.read(IntegrationTestConstant.PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON);
        var giftCertificateJsonAsString = new ObjectMapper().writeValueAsString(giftCertificateJson);

        //WHEN
        var id = getCreatedGiftCertificateId(giftCertificateJsonAsString, pageNumber);
        mockMvc.perform(MockMvcRequestBuilders
                .delete(IntegrationTestConstant.DELETE_GIFT_CERTIFICATE_URL, id))
                .andExpect(status().isOk());
    }

    private void clearDatabase(WebApplicationContext webApplicationContext) throws SQLException {
        try(Connection connection = webApplicationContext.getBean(DataSource.class).getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(IntegrationTestConstant.CLEAR_DATABASE_QUERY);
        }
    }

    private long getCreatedGiftCertificateId(String jsonNodeAsString, int pageNumber) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(IntegrationTestConstant.CREATE_GIFT_CERTIFICATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNodeAsString))
                .andExpect(status().isOk());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(IntegrationTestConstant.GET_LIST_OF_GIFT_CERTIFICATES_FOR_PAGE_URL, pageNumber))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get(0)
                .get(IntegrationTestConstant.GIFT_CERTIFICATE_FIELD_ID_KEY).asLong();
    }
}
