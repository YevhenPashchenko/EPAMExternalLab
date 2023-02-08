package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificateApplication;
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
@SpringBootTest(classes = GiftCertificateApplication.class)
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;
  private final JsonFileReader jsonFileReader = new JsonFileReader();
  private final String PATTERN_BEFORE = "createDate";
  private final String PATTERN_AFTER = "userDto";

  @BeforeEach
  public void execute() {
    jdbcTemplate.execute(SqlConstant.TRUNCATE_TABLES);
    jdbcTemplate.execute(SqlConstant.RESTART_GIFT_CERTIFICATE_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_ORDER_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_TAG_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.RESTART_USER_ID_SEQUENCE);
    jdbcTemplate.execute(SqlConstant.CREATE_USER);
  }

  @Test
  void shouldReturnOrderDtoEntityModel_whenCreateGiftCertificate() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var orderDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_ORDER_DTO);

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
                MockMvcRequestBuilders.post(UrlConstant.CREATE_ORDER_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderDtoJsonAsString))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
        result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
            + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_ORDER_DTO_ENTITY_MODEL),
        resultWithoutCreateDate);
  }

  @Test
  void shouldReturnOrderDtoPagedModel_whenGetAllOrderDto() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var orderDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_ORDER_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ALL_ORDER_DTO_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
        result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
            + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_ORDER_DTO_PAGED_MODEL),
        resultWithoutCreateDate);
  }

  @Test
  void shouldReturnOrderDtoEntityModel_whenGetOrderDtoByIdThatExists() throws Exception {
    // GIVEN
    var giftCertificateDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_GIFT_CERTIFICATE_DTO);
    var orderDtoJsonAsString =
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_CONSUMES_ORDER_DTO);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_GIFT_CERTIFICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(giftCertificateDtoJsonAsString))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(UrlConstant.CREATE_ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderDtoJsonAsString))
        .andExpect(status().isOk());
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ORDER_DTO_BY_ID))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    var result = mvcResult.getResponse().getContentAsString();
    var resultWithoutCreateDate =
            result.substring(0, result.indexOf(PATTERN_BEFORE) - 1)
                    + result.substring(result.indexOf(PATTERN_AFTER) - 1);
    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_ORDER_DTO_ENTITY_MODEL),
            resultWithoutCreateDate);
  }

  @Test
  void shouldReturnErrorDto_whenGetOrderDtoByIdThatNotExists() throws Exception {
    var mvcResult =
            mockMvc
                    .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ORDER_DTO_BY_ID))
                    .andExpect(status().isNotFound())
                    .andReturn();

    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_ORDER_ERROR_DTO),
            mvcResult.getResponse().getContentAsString());
  }
}
