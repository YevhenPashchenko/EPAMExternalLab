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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

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
    jdbcTemplate.execute(SqlConstant.CREATE_PERSON);
  }

  @Test
  void shouldReturnPersonDtoPagedModel_whenGetAllPersons() throws Exception {
    // WHEN
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get(UrlConstant.GET_ALL_PERSONS_URL))
            .andExpect(status().isOk())
            .andReturn();

    // THEN
    assertEquals(
        jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_PERSON_DTO_PAGED_MODEL),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnPersonDtoEntityModel_whenGetPersonByIdThatExists() throws Exception {
    // WHEN
    var mvcResult =
            mockMvc
                    .perform(MockMvcRequestBuilders.get(UrlConstant.GET_PERSON_BY_ID_URL))
                    .andExpect(status().isOk())
                    .andReturn();

    // THEN
    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_PERSON_DTO_ENTITY_MODEL),
            mvcResult.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnErrorDto_whenGetPersonByIdThatNotExists() throws Exception {
    // WHEN
    var mvcResult =
            mockMvc
                    .perform(MockMvcRequestBuilders.get(UrlConstant.GET_PERSON_BY_ID_NOT_EXIST_URL))
                    .andExpect(status().isNotFound())
                    .andReturn();

    // THEN
    assertEquals(
            jsonFileReader.readJsonAsString(PathConstant.PATH_TO_PRODUCES_PERSON_ERROR_DTO),
            mvcResult.getResponse().getContentAsString());
  }
}
