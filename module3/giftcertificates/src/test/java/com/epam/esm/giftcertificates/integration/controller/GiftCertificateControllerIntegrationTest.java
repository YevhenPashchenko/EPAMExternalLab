package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificatesApplication;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.integration.constant.TestEntityFieldValues;
import com.epam.esm.giftcertificates.integration.constant.TestFilePaths;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
@Transactional
class GiftCertificateControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @BeforeEach
    public void execute() {
        giftCertificateRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    void create_shouldReturnCreatedGiftCertificate_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.GIFT_CERTIFICATES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(getGiftCertificateMatchers());

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
        assertGiftCertificate(giftCertificateRepository.findAll().get(0));
    }

    private void assertGiftCertificate(GiftCertificate giftCertificate) {
        assertAll(
            () -> assertThat(giftCertificate.getName()).isEqualTo(TestEntityFieldValues.NAME),
            () -> assertThat(giftCertificate.getDescription()).isEqualTo(TestEntityFieldValues.DESCRIPTION),
            () -> assertThat(giftCertificate.getPrice()).isEqualTo(TestEntityFieldValues.PRICE),
            () -> assertThat(giftCertificate.getDuration()).isEqualTo(TestEntityFieldValues.DURATION),
            () -> assertThat(giftCertificate.getTags()).hasSize(2)
        );
    }

    private ResultMatcher[] getGiftCertificateMatchers() {
        return new ResultMatcher[]{
            jsonPath("$.name").value(TestEntityFieldValues.NAME),
            jsonPath("$.description").value(TestEntityFieldValues.DESCRIPTION),
            jsonPath("$.price").value(TestEntityFieldValues.PRICE),
            jsonPath("$.duration").value(TestEntityFieldValues.DURATION),
            jsonPath("$.tags.size()").value(2)
        };
    }

    @Test
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankTagName() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_TAG_NAME);

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
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankGiftCertificateName() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_GIFT_CERTIFICATE_NAME);

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
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankGiftCertificateDescription() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_GIFT_CERTIFICATE_DESCRIPTION);

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
    void create_shouldReturn400_whenCalledCreateEndpointWithGiftCertificatePriceLessThanZeroDotOne() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJson(
                TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_PRICE_LESS_THAN_ZERO_DOT_ONE);

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
    void create_shouldReturn400_whenCalledCreateEndpointWithGiftCertificatePriceScaleMoreThanTwo() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(
            TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_PRICE_SCALE_MORE_THAN_TWO);

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
    void create_shouldReturn400_whenCalledCreateEndpointWithGiftCertificateDurationLessThanOne() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJson(
                TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_DURATION_LESS_THAN_ONE);

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
    void getAll_shouldReturnPageOfGiftCertificates_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        giftCertificateRepository.save(TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME,
            TestEntityFieldValues.DESCRIPTION, TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION,
            Set.of(tag1, tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(getPageOfGiftCertificateMatchers());

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
        assertGiftCertificate(giftCertificateRepository.findAll().get(0));
    }

    private ResultMatcher[] getPageOfGiftCertificateMatchers() {
        return new ResultMatcher[]{jsonPath("$._embedded.giftCertificateDtoList.size()").value(1),
            jsonPath("$._embedded.giftCertificateDtoList[0].name").value(TestEntityFieldValues.NAME),
            jsonPath("$._embedded.giftCertificateDtoList[0].description").value(TestEntityFieldValues.DESCRIPTION),
            jsonPath("$._embedded.giftCertificateDtoList[0].price").value(TestEntityFieldValues.PRICE),
            jsonPath("$._embedded.giftCertificateDtoList[0].duration").value(TestEntityFieldValues.DURATION),
            jsonPath("$._embedded.giftCertificateDtoList[0].tags.size()").value(2)};
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidPageOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    private ResultMatcher[] getNotValidPageOfPaginationMatchers() {
        return new ResultMatcher[]{
            jsonPath("$.code").value(400),
            jsonPath("$.message").value("Page index must not be less than zero")
        };
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidSizeOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    private ResultMatcher[] getNotValidSizeOfPaginationMatchers() {
        return new ResultMatcher[]{
            jsonPath("$.code").value(400),
            jsonPath("$.message").value("Page size must not be less than one")
        };
    }

    @Test
    void getById_shouldReturnGiftCertificate_whenCalledGetByIdEndpointWithValidId() throws Exception {
        // GIVEN
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var giftCertificate =
            giftCertificateRepository.save(TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME,
                TestEntityFieldValues.DESCRIPTION, TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION,
                Set.of(tag1, tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getId()))
            .andExpect(status().isOk())
            .andExpectAll(getGiftCertificateMatchers());

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
        assertGiftCertificate(giftCertificateRepository.findAll().get(0));
    }

    @Test
    void getById_shouldReturn404_whenCalledGetByIdEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + "/" + id))
            .andExpect(status().isNotFound())
            .andExpectAll(getErrorMatchers(id));

        assertThat(giftCertificateRepository.count()).isZero();
    }

    private ResultMatcher[] getErrorMatchers(Long id) {
        return new ResultMatcher[]{
            jsonPath("$.code").value(40401),
            jsonPath("$.message").value("Requested resource not found (id = " + id + ")")
        };
    }

    @Test
    void getAllByParameters_shouldReturnPageOfGiftCertificates_whenCalledGetAllByParametersEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_SORTING_PARAMETERS);
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        giftCertificateRepository.save(TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME,
            TestEntityFieldValues.DESCRIPTION, TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION,
            Set.of(tag1, tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GET_ALL_GIFT_CERTIFICATES_BY_PARAMETERS_URL + TestUrls.VALID_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(getPageOfGiftCertificateMatchers());

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
        assertGiftCertificate(giftCertificateRepository.findAll().get(0));
    }

    @Test
    void getAllByParameters_shouldReturn400_whenCalledGetAllByParametersEndpointWithNotValidPageOfPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_SORTING_PARAMETERS);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GET_ALL_GIFT_CERTIFICATES_BY_PARAMETERS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidPageOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    void getAllByParameters_shouldReturn400_whenCalledGetAllByParametersEndpointWithNotValidSizeOfPagination()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_SORTING_PARAMETERS);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(
                    TestUrls.GET_ALL_GIFT_CERTIFICATES_BY_PARAMETERS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidSizeOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    void getAllByTags_shouldReturnPageOfGiftCertificates_whenCalledGetAllAllByTagsEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var tag3 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.THIRD_TAG_NAME));
        giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));
        giftCertificateRepository.save(TestEntityFactory.createGiftCertificate(TestEntityFieldValues.SECOND_NAME,
            TestEntityFieldValues.SECOND_DESCRIPTION, TestEntityFieldValues.SECOND_PRICE,
            TestEntityFieldValues.SECOND_DURATION, Set.of(tag2, tag3)));
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificateDto();
        giftCertificate.setTags(List.of(entityDtoMapper.tagToTagDto(tag1), entityDtoMapper.tagToTagDto(tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.VALID_PAGINATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReader.writeJson(giftCertificate)))
            .andExpect(status().isOk())
            .andExpectAll(getPageOfGiftCertificateMatchers());

        assertThat(giftCertificateRepository.count()).isEqualTo(2);
    }

    @Test
    void getAllByTags_shouldReturn400_whenCalledGetAllAllByTagsEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonReader.writeJson(TestEntityFactory.createDefaultGiftCertificateDto())))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidPageOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    void getAllByTags_shouldReturn400_whenCalledGetAllAllByTagsEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.GIFT_CERTIFICATES_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonReader.writeJson(TestEntityFactory.createDefaultGiftCertificateDto())))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidSizeOfPaginationMatchers());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    void update_shouldReturnUpdatedGiftCertificate_whenCalledUpdateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_FOR_UPDATE);
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var giftCertificate = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(jsonPath("$.name").value(TestEntityFieldValues.SECOND_NAME),
                jsonPath("$.description").value(TestEntityFieldValues.SECOND_DESCRIPTION),
                jsonPath("$.price").value(TestEntityFieldValues.SECOND_PRICE),
                jsonPath("$.duration").value(TestEntityFieldValues.SECOND_DURATION),
                jsonPath("$.tags.size()").value(2)
            );

        assertThat(giftCertificateRepository.count()).isEqualTo(1);
        assertAll(
            () -> assertThat(giftCertificate.getName()).isEqualTo(TestEntityFieldValues.SECOND_NAME),
            () -> assertThat(giftCertificate.getDescription()).isEqualTo(TestEntityFieldValues.SECOND_DESCRIPTION),
            () -> assertThat(giftCertificate.getPrice()).isEqualTo(TestEntityFieldValues.SECOND_PRICE),
            () -> assertThat(giftCertificate.getDuration()).isEqualTo(TestEntityFieldValues.SECOND_DURATION),
            () -> assertThat(giftCertificate.getTags()).hasSize(2)
        );
    }

    @Test
    void update_shouldReturnErrorDto_whenCalledUpdateEndpointWithNotValidId() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_GIFT_CERTIFICATE_FOR_UPDATE);
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.GIFT_CERTIFICATES_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(getErrorMatchers(id));
    }

    @Test
    void delete_shouldReturn200_whenCalledDeleteEndpointWithValidId() throws Exception {
        // GIVEN
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var giftCertificate = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + giftCertificate.getId()))
            .andExpect(status().isOk());

        assertThat(giftCertificateRepository.count()).isZero();
    }

    @Test
    void delete_shouldReturn400_whenCalledDeleteEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.GIFT_CERTIFICATES_URL + "/" + id))
            .andExpect(status().isBadRequest());
    }
}
