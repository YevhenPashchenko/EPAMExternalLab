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
class TagControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;
  private final JsonFileReader jsonFileReader = new JsonFileReader();

  @BeforeEach
  public void execute() {
    jdbcTemplate.execute(SqlConstant.TRUNCATE_TABLES);
    jdbcTemplate.execute(SqlConstant.RESTART_GIFT_CERTIFICATE_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_RECIPE_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_TAG_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_PERSON_ID_SEQUENCE);
  }

  @Test
  void shouldReturnTagDtoEntityModel_whenCreateTag() throws Exception {
    // GIVEN
    var tagDtoAsString = jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_TAG_DTO);

    // WHEN
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(UrlConstant.CREATE_TAG_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(tagDtoAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_TAG_DTO_ENTITY_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnTagDtoPagedModel_whenGetAllTags() throws Exception {
    // GIVEN
    var tagDtoAsString = jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_TAG_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_TAG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tagDtoAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ALL_TAGS_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_TAG_DTO_PAGED_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnTagDtoEntityModel_whenGetTagByIdThatExists() throws Exception {
    // GIVEN
    var tagDtoAsString = jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_TAG_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_TAG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tagDtoAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_TAG_BY_ID_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_TAG_DTO_ENTITY_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnErrorDto_whenTagByIdThatNotExists() throws Exception {
    // WHEN
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_TAG_BY_ID_URL))
            .andExpect(status().isNotFound())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_TAG_ERROR_DTO),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnTagDtoPagedModel_whenGetMostWidelyUsedTagsFromPersonMaxCostRecipe()
      throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var recipeDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_RECIPE_DTO);

    // WHEN
    jdbcTemplate.execute(SqlConstant.CREATE_PERSON);
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
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_MOST_WIDELY_USED_TAGS))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_MOST_WIDELY_USED_TAG_DTO_PAGED_MODEL),
            mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturn200_whenDeleteTagThatExists() throws Exception {
    // GIVEN
    var tagDtoAsString = jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_TAG_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_TAG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tagDtoAsString))
        .andExpect(status().isOk());

    // THEN
    mockMvc
        .perform(MockMvcRequestBuilders.delete(UrlConstant.DELETE_TAG_URL))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn400_whenDeleteTagThatNotExists() throws Exception {
    // THEN
    mockMvc
        .perform(MockMvcRequestBuilders.delete(UrlConstant.DELETE_TAG_URL))
        .andExpect(status().isBadRequest());
  }
}
