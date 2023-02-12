package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificatesApplication;
import com.epam.esm.giftcertificates.integration.constant.PathConstant;
import com.epam.esm.giftcertificates.integration.constant.SqlConstant;
import com.epam.esm.giftcertificates.integration.constant.UrlConstant;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonFileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
class RecipeControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;
  private final JsonFileReader jsonFileReader = new JsonFileReader();
  private final String PATTERN_BEFORE = "createDate";
  private final String PATTERN_AFTER = "personDto";

  @BeforeEach
  public void execute() {
    jdbcTemplate.execute(SqlConstant.TRUNCATE_TABLES);
    jdbcTemplate.execute(SqlConstant.RESTART_GIFT_CERTIFICATE_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_RECIPE_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_TAG_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_PERSON_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.CREATE_PERSON);
  }

  @Test
  void shouldReturnRecipeDtoEntityModel_whenCreateRecipe() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var recipeDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_RECIPE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(UrlConstant.CREATE_RECIPE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(recipeDtoJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
        result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
            + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_RECIPE_DTO_ENTITY_MODEL),
        resultWithoutCreateDate);
  }

  @Test
  void shouldReturnRecipeDtoPagedModel_whenGetAllRecipes() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var recipeDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_RECIPE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_RECIPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ALL_RECIPES_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
        result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
            + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_RECIPE_DTO_PAGED_MODEL),
        resultWithoutCreateDate);
  }

  @Test
  void shouldReturnRecipeDtoEntityModel_whenGetRecipeByIdThatExists() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var recipeDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_RECIPE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_RECIPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_RECIPE_BY_ID))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
            result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
                    + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_RECIPE_DTO_ENTITY_MODEL),
            resultWithoutCreateDate);
  }

  @Test
  void shouldReturnErrorDto_whenGetRecipeByIdThatNotExists() throws Exception {
    var mvcResult =
            mockMvc
                    .perform(MockMvcRequestBuilders.get(UrlConstant.GET_RECIPE_BY_ID))
                    .andExpect(status().isNotFound())
                    .andReturn();

    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_RECIPE_ERROR_DTO),
            mvcResult.getResponse().getContentAsString());
  }
}
