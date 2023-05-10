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
import com.epam.esm.giftcertificates.repository.TagRepository;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftcertificatesApplication.class)
@AutoConfigureMockMvc
class TagControllerIntegrationTest {

    private static final String TAG1_NAME = "tagName1";
    private static final String TAG2_NAME = "tagName2";
    private static final String TAG3_NAME = "tagName3";
    private static final String NAME = "name2";
    private static final BigDecimal PRICE = BigDecimal.valueOf(120.00);
    private static final String EMAIL = "user1@mail.com";
    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    @BeforeEach
    public void clear() {
        tagRepository.deleteAllInBatch();
        giftCertificateRepository.deleteAllInBatch();
        receiptRepository.deleteAllInBatch();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.TAGS_WRITE})
    void create_shouldReturnCreatedTag_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG);
        var tagDto = TestEntityFactory.createDefaultTagDto(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(tagDto.getName()));

        var tag = tagRepository.findAll().get(0);
        assertAll(
            () -> assertThat(tagRepository.count()).isEqualTo(1),
            () -> assertThat(tag.getName()).isEqualTo(tagDto.getName())
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutTagsWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_WRITE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.TAGS_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankTagName() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG_WITH_BLANK_TAG_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("name must not be empty")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.TAGS_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithTagNameThatAlreadyExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG);
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);
        tagRepository.save(tag);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value(
                    "name=" + tag.getName() + " already exists.")
            );

        assertThat(tagRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_READ})
    void getAll_shouldReturnTags_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);
        tagRepository.save(tag);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.tagDtoList[0].name").value(tag.getName()),
                jsonPath("$._embedded.tagDtoList.size()").value(1)
            );

        assertThat(tagRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutTagsReadScope() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_READ})
    void getByName_shouldReturnTag_whenCalledGetByNameEndpointWithNameThatExist() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);
        tagRepository.save(tag);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(tag.getName())
            );

        assertThat(tagRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void getByName_shouldReturn403_whenCalledGetByNameEndpointWithoutTagsReadScope() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_READ})
    void getByName_shouldReturn404_whenCalledGetByNameEndpointWithNameThatNotExist() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.TAG_NOT_FOUND),
                jsonPath("$.message").value("Requested tag not found (name = " + tag.getName() + ")")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.TAGS_READ})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturnTags_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        saveReceipts();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.tagDtoList.size()").value(2),
                jsonPath("$._embedded.tagDtoList[0].name").value(TAG1_NAME),
                jsonPath("$._embedded.tagDtoList[1].name").value(TAG2_NAME)
            );

        assertAll(
            () -> assertThat(tagRepository.count()).isEqualTo(3),
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(2),
            () -> assertThat(receiptRepository.count()).isEqualTo(2)
        );
    }

    private void saveReceipts() {
        var tag1 = TestEntityFactory.createDefaultTag(TAG1_NAME);
        var tag2 = TestEntityFactory.createDefaultTag(TAG2_NAME);
        var tag3 = TestEntityFactory.createDefaultTag(TAG3_NAME);
        var giftCertificate1 = TestEntityFactory.createDefaultGiftCertificate();
        var giftCertificate2 = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate2.setName(NAME);
        giftCertificate2.setPrice(PRICE);
        var receipt1 =
            TestEntityFactory.createDefaultReceipt(List.of(giftCertificate1, giftCertificate1, giftCertificate2),
                EMAIL);
        var receipt2 =
            TestEntityFactory.createDefaultReceipt(List.of(giftCertificate1, giftCertificate2, giftCertificate2),
                EMAIL);
        giftCertificate1.addTag(tag1);
        giftCertificate1.addTag(tag2);
        giftCertificate2.addTag(tag3);
        giftCertificateRepository.saveAll(List.of(giftCertificate1, giftCertificate2));
        receiptRepository.saveAll(List.of(receipt1, receipt2));
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.TAGS_READ})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn403_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWithoutAdminOrUserRole()
        throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertAll(
            () -> assertThat(tagRepository.count()).isZero(),
            () -> assertThat(giftCertificateRepository.count()).isZero(),
            () -> assertThat(receiptRepository.count()).isZero()
        );
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn403_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWithoutTagsReadScope()
        throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertAll(
            () -> assertThat(tagRepository.count()).isZero(),
            () -> assertThat(giftCertificateRepository.count()).isZero(),
            () -> assertThat(receiptRepository.count()).isZero()
        );
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.TAGS_READ})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn400_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWithNotValidPageOfPagination()
        throws Exception {
        // GIVEN
        saveReceipts();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertAll(
            () -> assertThat(tagRepository.count()).isEqualTo(3),
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(2),
            () -> assertThat(receiptRepository.count()).isEqualTo(2)
        );
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.TAGS_READ})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn400_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWithNotValidSizeOfPagination()
        throws Exception {
        // GIVEN
        saveReceipts();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertAll(
            () -> assertThat(tagRepository.count()).isEqualTo(3),
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(2),
            () -> assertThat(receiptRepository.count()).isEqualTo(2)
        );
    }

    @Test
    @WithMockUser(username = EMAIL, authorities = {Authorities.USER_ROLE, Authorities.TAGS_READ})
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn404_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostReceiptEndpointWhenAnyReceiptNotExist()
        throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.WIDELY_USED_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.TAG_NOT_FOUND),
                jsonPath("$.message").value("Most widely used tag in recipe with max cost not found")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.TAGS_WRITE})
    void delete_shouldReturnTag_whenCalledDeleteEndpointWithNameThatExist() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(tag);
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(tag.getName())
            );

        assertAll(
            () -> assertThat(tagRepository.count()).isZero(),
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(1)
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutTagsWriteScope() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.TAGS_WRITE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isForbidden());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.TAGS_WRITE})
    void delete_shouldReturn404_whenCalledDeleteEndpointWithNameThatNotExist() throws Exception {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + tag.getName()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.TAG_NOT_FOUND),
                jsonPath("$.message").value("Requested tag not found (name = " + tag.getName() + ")")
            );

        assertThat(tagRepository.count()).isZero();
    }
}
