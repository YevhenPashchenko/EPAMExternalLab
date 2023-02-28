package com.epam.esm.giftcertificates.integration.controller;

import com.epam.esm.giftcertificates.GiftCertificatesApplication;
import com.epam.esm.giftcertificates.integration.constant.TestEntityFieldValues;
import com.epam.esm.giftcertificates.integration.constant.TestFilePaths;
import com.epam.esm.giftcertificates.integration.constant.TestUrls;
import com.epam.esm.giftcertificates.integration.container.PostgreSqlTestContainer;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.repository.PersonRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = GiftCertificatesApplication.class)
@AutoConfigureMockMvc
class TagControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    @BeforeEach
    public void execute() {
        tagRepository.deleteAll();
        personRepository.deleteAll();
        giftCertificateRepository.deleteAll();
    }

    @Test
    void create_shouldReturnCreatedTag_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_TAG);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL).contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isOk()).andExpect(
            jsonPath("$.name").value(TestEntityFieldValues.FIRST_TAG_NAME)
        );

        var tag = tagRepository.findAll().get(0);
        assertThat(tagRepository.count()).isEqualTo(1);
        assertThat(tag.getName()).isEqualTo(TestEntityFieldValues.FIRST_TAG_NAME);
    }

    @Test
    void create_shouldReturn400_whenCalledCreateEndpointWithBlankTagName() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJson(TestFilePaths.PATH_TO_INPUT_TAG_WITH_BLANK_NAME);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.TAGS_URL).contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isBadRequest()).andExpectAll(
            jsonPath("$.code").value(400),
            jsonPath("$.message").value("name must not be empty")
        );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    void getAll_shouldReturnPageOfTags_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.tagDtoList[0].name").value(TestEntityFieldValues.FIRST_TAG_NAME),
                jsonPath("$._embedded.tagDtoList.size()").value(1)
            );

        var tag = tagRepository.findAll().get(0);
        assertThat(tagRepository.count()).isEqualTo(1);
        assertThat(tag.getName()).isEqualTo(TestEntityFieldValues.FIRST_TAG_NAME);
    }

    @Test
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidPageOfPaginationMatchers());

        assertThat(tagRepository.count()).isZero();
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
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(getNotValidSizeOfPaginationMatchers());

        assertThat(tagRepository.count()).isZero();
    }

    private ResultMatcher[] getNotValidSizeOfPaginationMatchers() {
        return new ResultMatcher[]{
            jsonPath("$.code").value(400),
            jsonPath("$.message").value("Page size must not be less than one")
        };
    }

    @Test
    void getById_shouldReturnTag_whenCalledGetByIdEndpointWithValidId() throws Exception {
        // GIVEN
        var tag = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + "/" + tag.getId()))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.name").value(TestEntityFieldValues.FIRST_TAG_NAME)
            );

        var result = tagRepository.findAll().get(0);
        assertThat(tagRepository.count()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(TestEntityFieldValues.FIRST_TAG_NAME);
    }

    @Test
    void getById_shouldReturnErrorDto_whenCalledGetByIdEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.TAGS_URL + "/" + id)).andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(40402),
                jsonPath("$.message").value("Requested resource not found (id = " + id + ")")
            );

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturnPageOfTag_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostRecipeEndpointWithValidPagination()
        throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var tag3 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.THIRD_TAG_NAME));
        var giftCertificate1 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));
        var giftCertificate2 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.SECOND_NAME,
                TestEntityFieldValues.SECOND_DESCRIPTION, TestEntityFieldValues.SECOND_PRICE,
                TestEntityFieldValues.SECOND_DURATION, Set.of(tag2, tag3)));
        receiptRepository.save(
            TestEntityFactory.createReceipt(TestEntityFieldValues.PRICE.add(TestEntityFieldValues.SECOND_PRICE), person,
                List.of(giftCertificate1, giftCertificate2)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.TAGS_URL + TestUrls.PERSONS_URL + "/" + person.getId() + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk()).andExpectAll(
                jsonPath("$._embedded.tagDtoList[0].name").value(TestEntityFieldValues.SECOND_TAG_NAME),
                jsonPath("$._embedded.tagDtoList.size()").value(1)
            );

        assertThat(tagRepository.count()).isEqualTo(3);
    }

    @Test
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn400_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostRecipeEndpointWithNotValidPageOfPagination()
        throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var tag3 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.THIRD_TAG_NAME));
        var giftCertificate1 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));
        var giftCertificate2 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.SECOND_NAME,
                TestEntityFieldValues.SECOND_DESCRIPTION, TestEntityFieldValues.SECOND_PRICE,
                TestEntityFieldValues.SECOND_DURATION, Set.of(tag2, tag3)));
        receiptRepository.save(
            TestEntityFactory.createReceipt(TestEntityFieldValues.PRICE.add(TestEntityFieldValues.SECOND_PRICE), person,
                List.of(giftCertificate1, giftCertificate2)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.TAGS_URL + TestUrls.PERSONS_URL + "/" + person.getId()
                        + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest()).andExpectAll(getNotValidPageOfPaginationMatchers());

        assertThat(tagRepository.count()).isEqualTo(3);
    }

    @Test
    void
    getMostWidelyUsedTagsFromPersonMaxCostReceipt_shouldReturn400_whenCalledGetMostWidelyUsedTagsFromPersonMaxCostRecipeEndpointWithNotValidSizeOfPagination()
        throws Exception {
        // GIVEN
        var person = personRepository.save(
            TestEntityFactory.createPerson(TestEntityFieldValues.EMAIL, TestEntityFieldValues.PASSWORD));
        var tag1 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));
        var tag2 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.SECOND_TAG_NAME));
        var tag3 = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.THIRD_TAG_NAME));
        var giftCertificate1 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.NAME, TestEntityFieldValues.DESCRIPTION,
                TestEntityFieldValues.PRICE, TestEntityFieldValues.DURATION, Set.of(tag1, tag2)));
        var giftCertificate2 = giftCertificateRepository.save(
            TestEntityFactory.createGiftCertificate(TestEntityFieldValues.SECOND_NAME,
                TestEntityFieldValues.SECOND_DESCRIPTION, TestEntityFieldValues.SECOND_PRICE,
                TestEntityFieldValues.SECOND_DURATION, Set.of(tag2, tag3)));
        receiptRepository.save(
            TestEntityFactory.createReceipt(TestEntityFieldValues.PRICE.add(TestEntityFieldValues.SECOND_PRICE), person,
                List.of(giftCertificate1, giftCertificate2)));

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.TAGS_URL + TestUrls.PERSONS_URL + "/" + person.getId()
                        + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest()).andExpectAll(getNotValidSizeOfPaginationMatchers());

        assertThat(tagRepository.count()).isEqualTo(3);
    }

    @Test
    void delete_shouldReturn200_whenCalledDeleteEndpointWithValidId() throws Exception {
        // GIVEN
        var tag = tagRepository.save(TestEntityFactory.createTag(TestEntityFieldValues.FIRST_TAG_NAME));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + tag.getId()))
            .andExpect(status().isOk());

        assertThat(tagRepository.count()).isZero();
    }

    @Test
    void delete_shouldReturn400_whenCalledDeleteEndpointWithNotValidId() throws Exception {
        // GIVEN
        var id = 0L;

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.TAGS_URL + "/" + id)).andExpect(status().isBadRequest());

        assertThat(tagRepository.count()).isZero();
    }
}
