package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificatesApplication;
import com.epam.esm.giftcertificates.integration.constant.TestEntityFieldValues;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.repository.PersonRepository;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
@Transactional
class ReceiptControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @BeforeEach
    public void execute() {
        receiptRepository.deleteAll();
        personRepository.deleteAll();
        giftCertificateRepository.deleteAll();
    }

    @Test
    void create_shouldReturnCreatedReceipt_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var giftCertificate = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Collections.emptySet()));
        var receipt = TestEntityFactory.createDefaultReceiptDto();
        receipt.setGiftCertificates(List.of(entityDtoMapper.giftCertificateToGiftCertificateDto(giftCertificate)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.post(TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.RECEIPTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonReader.writeJson(receipt))).andExpect(status().isOk())
            .andExpectAll(getReceiptMatchers());

        var result = receiptRepository.findAll().get(0);
        assertThat(receiptRepository.count()).isEqualTo(1);
        assertThat(result.getTotalCost()).isEqualTo(TestEntityFieldValues.PRICE);
        assertThat(result.getGiftCertificates()).hasSize(1);
    }

    private ResultMatcher[] getReceiptMatchers() {
        return new ResultMatcher[]{
            jsonPath("$.totalCost").value(TestEntityFieldValues.PRICE),
            jsonPath("$.giftCertificates.size()").value(1)
        };
    }

    @Test
    void create_shouldReturn400_whenCalledCreateEndpointWithEmptyListOfGiftCertificate() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var receipt = TestEntityFactory.createDefaultReceiptDto();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.post(TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.RECEIPTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonReader.writeJson(receipt))).andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("list of gift certificates must not be empty")
            );

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    void getAll_shouldReturnPageOfReceipts_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var giftCertificate = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Collections.emptySet()));
        receiptRepository.save(
            TestEntityFactory.createReceipt(TestEntityFieldValues.PRICE, person, List.of(giftCertificate)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.RECEIPTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk()).andExpectAll(
                jsonPath("$._embedded.receiptDtoList[0].totalCost").value(TestEntityFieldValues.PRICE),
                jsonPath("$._embedded.receiptDtoList[0].giftCertificates.size()").value(1)
            );

        var receipt = receiptRepository.findAll().get(0);
        assertThat(receiptRepository.count()).isEqualTo(1);
        assertThat(receipt.getTotalCost()).isEqualTo(TestEntityFieldValues.PRICE);
        assertThat(receipt.getGiftCertificates()).hasSize(1);
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.RECEIPTS_URL
                        + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.RECEIPTS_URL
                        + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    void getById_shouldReturnReceipt_whenCalledGetByIdEndpointWithValidId() throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var giftCertificate = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Collections.emptySet()));
        var receipt = receiptRepository.save(
            TestEntityFactory.createReceipt(TestEntityFieldValues.PRICE, person, List.of(giftCertificate)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.RECEIPTS_URL + "/" + receipt.getId()))
            .andExpect(status().isOk())
            .andExpectAll(getReceiptMatchers());

        var result = receiptRepository.findAll().get(0);
        assertThat(receiptRepository.count()).isEqualTo(1);
        assertThat(result.getTotalCost()).isEqualTo(TestEntityFieldValues.PRICE);
        assertThat(result.getGiftCertificates()).hasSize(1);
    }

    @Test
    void getById_shouldReturnErrorDto_whenCalledGetByIdEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.RECEIPTS_URL + "/" + id))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(40404),
                jsonPath("$.message").value("Requested resource not found (id = " + id + ")")
            );

        assertThat(receiptRepository.count()).isZero();
    }
}
