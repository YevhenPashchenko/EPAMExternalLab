package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftcertificatesApplication;
import com.epam.esm.giftcertificates.constant.Authorities;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.integration.constant.TestFilePaths;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
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
class GiftCertificateControllerIntegrationTest {

    private static final String TAG_NAME = "tagName1";
    private static final String TAG2_NAME = "tagName2";
    private static final String TAG3_NAME = "tagName3";
    private static final String UPDATE_NAME = "name2";
    private static final String UPDATE_DESCRIPTION = "description2";
    private static final BigDecimal UPDATE_PRICE = BigDecimal.valueOf(100.50);
    private static final int UPDATE_DURATION = 5;
    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    public void clear() {
        giftCertificateRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturnCreatedGiftCertificate_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE);
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        var tag = TestEntityFactory.createDefaultTag(TAG_NAME);
        tagRepository.save(tag);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(giftCertificateDto.getName()),
                jsonPath("$.description").value(giftCertificateDto.getDescription()),
                jsonPath("$.price").value(giftCertificateDto.getPrice()),
                jsonPath("$.duration").value(giftCertificateDto.getDuration()),
                jsonPath("$.tags.size()").value(2)
            );

        var giftCertificate = giftCertificateRepository.findAll().get(0);
        assertAll(
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(1),
            () -> assertThat(giftCertificate.getName()).isEqualTo(giftCertificateDto.getName()),
            () -> assertThat(giftCertificate.getDescription()).isEqualTo(giftCertificateDto.getDescription()),
            () -> assertThat(giftCertificate.getPrice()).isEqualTo(giftCertificateDto.getPrice()),
            () -> assertThat(giftCertificate.getDuration()).isEqualTo(giftCertificateDto.getDuration()),
            () -> assertThat(giftCertificate.getTags()).hasSize(2)
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn403_whenCalledCreateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankTagName() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_TAG_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("name must not be empty")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankName() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("name must not be empty")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankDescription() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_DESCRIPTION);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("description must not be empty")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithPriceLessThanZeroDotOne() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_PRICE_LESS_THAN_ZERO_DOT_ONE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("price must be greater than 0")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithPriceScaleMoreThanTwo() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_PRICE_SCALE_MORE_THAN_TWO);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("number is out of range (expected <7 integer>.<2 fraction>)")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void create_shouldReturn400_whenCalledCreateEndpointWithDurationLessThanOne() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_WITH_DURATION_LESS_THAN_ONE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("duration must be greater than 0")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAll_shouldReturnGiftCertificates_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(TestEntityFactory.createDefaultTag(TAG_NAME));
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.giftCertificateDtoList.size()").value(1),
                jsonPath("$._embedded.giftCertificateDtoList[0].name").value(giftCertificate.getName()),
                jsonPath("$._embedded.giftCertificateDtoList[0].description").value(giftCertificate.getDescription()),
                jsonPath("$._embedded.giftCertificateDtoList[0].price").value(giftCertificate.getPrice()),
                jsonPath("$._embedded.giftCertificateDtoList[0].duration").value(giftCertificate.getDuration()),
                jsonPath("$._embedded.giftCertificateDtoList[0].tags.size()").value(giftCertificate.getTags().size())
            );

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutGiftCertificatesReadScope() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getByName_shouldReturnGiftCertificate_whenCalledGetByNameEndpointWithNameThatExist() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(TestEntityFactory.createDefaultTag(TAG_NAME));
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(giftCertificate.getName()),
                jsonPath("$.description").value(giftCertificate.getDescription()),
                jsonPath("$.price").value(giftCertificate.getPrice()),
                jsonPath("$.duration").value(giftCertificate.getDuration()),
                jsonPath("$.tags.size()").value(giftCertificate.getTags().size())
            );

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getByName_shouldReturn404_whenCalledGetByNameEndpointWithNameThatNotExist() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.GIFT_CERTIFICATE_NOT_FOUND),
                jsonPath("$.message").value(
                    "Requested gift certificate not found (name = " + giftCertificate.getName() + ")")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByParameters_shouldReturnGiftCertificates_whenCalledGetAllByParametersEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_SORTING_PARAMETERS);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(TestEntityFactory.createDefaultTag(TAG_NAME));
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GIFT_CERTIFICATES_URL + TestUrls.SORT_URL + TestUrls.VALID_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.giftCertificateDtoList.size()").value(1),
                jsonPath("$._embedded.giftCertificateDtoList[0].name").value(giftCertificate.getName()),
                jsonPath("$._embedded.giftCertificateDtoList[0].description").value(giftCertificate.getDescription()),
                jsonPath("$._embedded.giftCertificateDtoList[0].price").value(giftCertificate.getPrice()),
                jsonPath("$._embedded.giftCertificateDtoList[0].duration").value(giftCertificate.getDuration()),
                jsonPath("$._embedded.giftCertificateDtoList[0].tags.size()").value(giftCertificate.getTags().size())
            );

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByParameters_shouldReturn400_whenCalledGetAllByParametersEndpointWithNotValidPageOfPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_SORTING_PARAMETERS);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GIFT_CERTIFICATES_URL + TestUrls.SORT_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByParameters_shouldReturn400_whenCalledGetAllByParametersEndpointWithNotValidSizeOfPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_SORTING_PARAMETERS);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GIFT_CERTIFICATES_URL + TestUrls.SORT_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByTags_shouldReturnGiftCertificates_whenCalledGetAllByTagsEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG_NAMES);
        var tag1 = TestEntityFactory.createDefaultTag(TAG_NAME);
        var tag2 = TestEntityFactory.createDefaultTag(TAG2_NAME);
        var tag3 = TestEntityFactory.createDefaultTag(TAG3_NAME);
        var giftCertificate1 = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate1.addTag(tag1);
        giftCertificate1.addTag(tag2);
        var giftCertificate2 = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate2.setName("name2");
        giftCertificate2.addTag(tag1);
        var giftCertificate3 = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate3.setName("name3");
        giftCertificate3.addTag(tag1);
        giftCertificate3.addTag(tag3);
        giftCertificateRepository.saveAll(List.of(giftCertificate1, giftCertificate2, giftCertificate3));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.giftCertificateDtoList.size()").value(1),
                jsonPath("$._embedded.giftCertificateDtoList[0].name").value(giftCertificate1.getName()),
                jsonPath("$._embedded.giftCertificateDtoList[0].description").value(giftCertificate1.getDescription()),
                jsonPath("$._embedded.giftCertificateDtoList[0].price").value(giftCertificate1.getPrice()),
                jsonPath("$._embedded.giftCertificateDtoList[0].duration").value(giftCertificate1.getDuration()),
                jsonPath("$._embedded.giftCertificateDtoList[0].tags.size()").value(giftCertificate1.getTags().size())
            );

        assertThat(giftCertificateRepository.count()).isEqualTo(3);
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByTags_shouldReturn400_whenCalledGetAllByTagsEndpointWithNotValidPageOfPagination() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG_NAMES);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByTags_shouldReturn400_whenCalledGetAllByTagsEndpointWithNotValidSizeOfPagination() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_TAG_NAMES);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_READ})
    void getAllByTags_shouldReturn400_whenCalledGetAllByTagsEndpointWithEmptyTagNamesSet() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_EMPTY_TAG_NAMES);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("List of tag names must not be empty")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void update_shouldReturnUpdatedGiftCertificate_whenCalledUpdateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_GIFT_CERTIFICATE);
        var tag1 = TestEntityFactory.createDefaultTag(TAG_NAME);
        var tag2 = TestEntityFactory.createDefaultTag(TAG2_NAME);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(tag1);
        giftCertificate.addTag(tag2);
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(UPDATE_NAME),
                jsonPath("$.description").value(UPDATE_DESCRIPTION),
                jsonPath("$.price").value(UPDATE_PRICE),
                jsonPath("$.duration").value(UPDATE_DURATION),
                jsonPath("$.tags.size()").value(1),
                jsonPath("$.tags[0].name").value(tag1.getName())
            );

        var updateGiftCertificate = giftCertificateRepository.findAll().get(0);
        assertAll(
            () -> assertThat(giftCertificateRepository.count()).isEqualTo(1),
            () -> assertThat(updateGiftCertificate.getName()).isEqualTo(UPDATE_NAME),
            () -> assertThat(updateGiftCertificate.getDescription()).isEqualTo(UPDATE_DESCRIPTION),
            () -> assertThat(updateGiftCertificate.getPrice()).isEqualTo(UPDATE_PRICE),
            () -> assertThat(updateGiftCertificate.getDuration()).isEqualTo(UPDATE_DURATION),
            () -> assertThat(updateGiftCertificate.getTags()).hasSize(1),
            () -> assertThat(updateGiftCertificate.getTags().iterator().next().getName()).isEqualTo(tag1.getName())
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_GIFT_CERTIFICATE);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_WRITE})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_GIFT_CERTIFICATE);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void update_shouldReturn400_whenCalledUpdateEndpointWithEmptyTagsSet() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_GIFT_CERTIFICATE_WITH_EMPTY_TAGS_SET);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("List of tag names must not be empty")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void update_shouldReturn404_whenCalledUpdateEndpointWithNameThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_GIFT_CERTIFICATE);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.GIFT_CERTIFICATE_NOT_FOUND),
                jsonPath("$.message").value(
                    "Requested gift certificate not found (name = " + giftCertificate.getName() + ")")
            );

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void delete_shouldReturnDeletedGiftCertificate_whenCalledDeleteEndpointWithNameThatExist() throws Exception {
        // GIVEN
        var tag1 = TestEntityFactory.createDefaultTag(TAG_NAME);
        var tag2 = TestEntityFactory.createDefaultTag(TAG2_NAME);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        giftCertificate.addTag(tag1);
        giftCertificate.addTag(tag2);
        giftCertificateRepository.save(giftCertificate);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(giftCertificate.getName()),
                jsonPath("$.description").value(giftCertificate.getDescription()),
                jsonPath("$.price").value(giftCertificate.getPrice()),
                jsonPath("$.duration").value(giftCertificate.getDuration()),
                jsonPath("$.tags.size()").value(giftCertificate.getTags().size())
            );

        assertAll(
            () -> assertThat(giftCertificateRepository.count()).isZero(),
            () -> assertThat(tagRepository.count()).isEqualTo(giftCertificate.getTags().size())
        );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutGiftCertificatesWriteScope() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.GIFT_CERTIFICATES_WRITE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.GIFT_CERTIFICATES_WRITE})
    void delete_shouldReturn404_whenCalledDeleteEndpointWithNameThatNotExist() throws Exception {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getName()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(HttpErrorCodes.GIFT_CERTIFICATE_NOT_FOUND),
                jsonPath("$.message").value(
                    "Requested gift certificate not found (name = " + giftCertificate.getName() + ")")
            );
    }
}
