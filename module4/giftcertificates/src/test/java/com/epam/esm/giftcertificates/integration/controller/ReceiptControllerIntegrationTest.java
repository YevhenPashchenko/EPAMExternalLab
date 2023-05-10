package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftcertificatesApplication;
import com.epam.esm.giftcertificates.constant.Authorities;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.integration.constant.TestFilePaths;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftcertificatesApplication.class)
class ReceiptControllerIntegrationTest {

    private static final String NAME = "name2";
    private static final BigDecimal PRICE = BigDecimal.valueOf(120.00);
    private static final String EMAIL = "user1@mail.com";
    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    public void clear() {
        receiptRepository.deleteAllInBatch();
        giftCertificateRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_WRITE})
    void create_shouldReturnCreatedReceipt_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_NAMES);
        var giftCertificate1 = TestEntityFactory.createDefaultGiftCertificate();
        var giftCertificate2 = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate2.setName(NAME);
        giftCertificate2.setPrice(PRICE);
        giftCertificateRepository.saveAll(List.of(giftCertificate1, giftCertificate2));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.RECEIPTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString())).andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.totalCost").value(giftCertificate1.getPrice().add(giftCertificate1.getPrice()).add(PRICE)),
                jsonPath("$.giftCertificates.size()").value(3)
            );

        var receipt = receiptRepository.findAll().get(0);
        assertAll(
            () -> assertThat(receiptRepository.count()).isEqualTo(1),
            () -> assertThat(receipt.getTotalCost()).isEqualTo(
                giftCertificate1.getPrice().add(giftCertificate1.getPrice()).add(PRICE)),
            () -> assertThat(receipt.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(receipt.getGiftCertificates()).hasSize(3)
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.USER_ROLE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_NAMES);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.RECEIPTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isForbidden());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.RECEIPTS_WRITE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutUserRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_NAMES);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.RECEIPTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isForbidden());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithEmptyGiftCertificateNamesList() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_EMPTY_GIFT_CERTIFICATE_NAMES);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.RECEIPTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString())).andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("list of gift certificate names must not be empty")
            );

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_READ})
    void getAll_shouldReturnCurrentUserReceipts_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        var receipt = TestEntityFactory.createDefaultReceipt(List.of(giftCertificate), EMAIL);
        giftCertificateRepository.save(giftCertificate);
        receiptRepository.save(receipt);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.receiptDtoList.size()").value(1),
                jsonPath("$._embedded.receiptDtoList[0].totalCost").value(receipt.getTotalCost()),
                jsonPath("$._embedded.receiptDtoList[0].giftCertificates.size()").value(1)
            );

        assertThat(receiptRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.USER_ROLE})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.RECEIPTS_READ})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutUserRole() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest());

        assertThat(receiptRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_READ})
    void getById_shouldReturnReceipt_whenCalledGetByIdEndpointWithIdThatExist() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificateRepository.save(giftCertificate);
        var receipt = receiptRepository.save(TestEntityFactory.createDefaultReceipt(List.of(giftCertificate), EMAIL));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + "/" + receipt.getId()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.totalCost").value(receipt.getTotalCost()),
                jsonPath("$.giftCertificates.size()").value(1)
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.USER_ROLE})
    void getById_shouldReturn403_whenCalledGetByIdEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + "/" + id))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.RECEIPTS_READ})
    void getById_shouldReturn403_whenCalledGetByIdEndpointWithoutUserRole() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + "/" + id))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.RECEIPTS_READ})
    void getById_shouldReturn404_whenCalledGetByIdEndpointWithIdThatNotExist() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.RECEIPTS_URL + "/" + id))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.RECEIPT_NOT_FOUND),
                jsonPath("$.message").value("Requested receipt not found (id = " + id + ")")
            );
    }
}
