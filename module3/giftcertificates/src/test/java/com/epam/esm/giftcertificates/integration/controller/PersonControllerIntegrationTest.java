package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificatesApplication;
import com.epam.esm.giftcertificates.integration.constant.TestEntityFieldValues;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.repository.PersonRepository;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void execute() {
        personRepository.deleteAll();
    }

    @Test
    void getAll_shouldReturnPageOfPersons_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // WHEN
        personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$._embedded.personDtoList[0].email").value(TestEntityFieldValues.EMAIL)
            );

        var person = personRepository.findAll().get(0);
        assertThat(personRepository.count()).isEqualTo(1);
        assertThat(person.getEmail()).isEqualTo(TestEntityFieldValues.EMAIL);
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    void getById_shouldReturnPerson_whenCalledGetByIdEndpointWithValidId() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + "/" + person.getId()))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.email").value(TestEntityFieldValues.EMAIL)
            );

        assertThat(personRepository.count()).isEqualTo(1);
        assertThat(person.getEmail()).isEqualTo(TestEntityFieldValues.EMAIL);
    }

    @Test
    void getById_shouldReturnErrorDto_whenCalledGetByIdEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + "/" + id))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(40403),
                jsonPath("$.message").value("Requested resource not found (id = " + id + ")")
            );

        assertThat(personRepository.count()).isZero();
    }
}
