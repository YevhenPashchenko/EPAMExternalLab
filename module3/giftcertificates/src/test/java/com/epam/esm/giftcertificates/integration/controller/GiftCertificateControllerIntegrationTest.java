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
class GiftCertificateControllerIntegrationTest {

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
  void shouldReturnGiftCertificateDtoEntityModel_whenCreateGiftCertificate() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);

    // WHEN
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCertificateDtoJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_ENTITY_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnGiftCertificateDtoPagedModel_whenGetAllGiftCertificates() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ALL_GIFT_CERTIFICATES))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_PAGED_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnGiftCertificateDtoEntityModel_whenGetGiftCertificateByIdThatExists()
      throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_GIFT_CERTIFICATE_BY_ID_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_ENTITY_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnErrorDto_whenGetGiftCertificateByIdThatNotExists() throws Exception {
    // WHEN
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_GIFT_CERTIFICATE_BY_ID_URL))
            .andExpect(status().isNotFound())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_ERROR_DTO),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnGiftCertificateDtoPagedModel_whenGetAllGiftCertificatesByParameters()
      throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var giftCertificateSortingParametersDtoJsonAsString =
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_SORTING_PARAMETERS_DTO);

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
                MockMvcRequestBuilders.get(
                        UrlConstant.GET_ALL_GIFT_CERTIFICATES_BY_PARAMETERS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCertificateSortingParametersDtoJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_BY_PARAMETERS_PAGED_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnGiftCertificateDtoPagedModel_whenGetAllGiftCertificatesByTags()
      throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(UrlConstant.GET_ALL_GIFT_CERTIFICATES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCertificateDtoJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
            jsonFileReader.readJsonAsString(
                    PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_BY_TAGS_PAGED_MODEL),
            mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnGiftCertificateDtoEntityModel_whenUpdateGiftCertificateThatExists()
      throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var giftCertificateDtoForUpdateJsonAsString =
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_FOR_UPDATE_DTO);

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
                MockMvcRequestBuilders.patch(UrlConstant.UPDATE_GIFT_CERTIFICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCertificateDtoForUpdateJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_DTO_ENTITY_MODEL_AFTER_UPDATE),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnErrorDto_whenUpdateGiftCertificateThatNotExists() throws Exception {
    // GIVEN
    var giftCertificateDtoForUpdateJsonAsString =
        jsonFileReader.readJsonAsString(
            PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_FOR_UPDATE_DTO);

    // WHEN
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.patch(UrlConstant.UPDATE_GIFT_CERTIFICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCertificateDtoForUpdateJsonAsString))
            .andExpect(status().isNotFound())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_GIFT_CERTIFICATE_ERROR_DTO),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturn200_whenDeleteGiftCertificateThatExists() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());

    // THEN
    mockMvc
        .perform(MockMvcRequestBuilders.delete(UrlConstant.DELETE_GIFT_CERTIFICATE_URL))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn400_whenDeleteGiftCertificateThatNotExists() throws Exception {
    // THEN
    mockMvc
        .perform(MockMvcRequestBuilders.delete(UrlConstant.DELETE_GIFT_CERTIFICATE_URL))
        .andExpect(status().isBadRequest());
  }
}
