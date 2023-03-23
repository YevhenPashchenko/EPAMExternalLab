package com.epam.esm.authorization.integration.controller;

import com.epam.esm.authorization.AuthorizationApplication;
import com.epam.esm.authorization.integration.constant.TestFilePaths;
import com.epam.esm.authorization.integration.constant.TestUrls;
import com.epam.esm.authorization.integration.container.PostgreSqlTestContainer;
import com.epam.esm.authorization.integration.reader.JsonReader;
import com.epam.esm.authorization.repository.PersonRepository;
import com.epam.esm.authorization.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = AuthorizationApplication.class)
class PersonControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void execute() {
        personRepository.deleteAll();
    }

    @Test
    void create_shouldReturn200_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_PERSON);
        var defaultPerson = TestEntityFactory.createDefaultPerson();

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk());

        var person = personRepository.findAll().get(0);
        assertAll(
            () -> assertThat(personRepository.count()).isEqualTo(1),
            () -> assertThat(person.getEmail()).isEqualTo(defaultPerson.getEmail()),
            () -> assertThat(passwordEncoder.matches(defaultPerson.getPassword(), person.getPassword())).isTrue()
        );
    }

    @Test
    void create_shouldReturn400_whenCalledCreateEndpointWithNotValidEmail() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_PERSON_WITH_NOT_VALID_EMAIL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("email not valid")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    void create_shouldReturn400_whenCalledCreateEndpointWithNotValidPassword() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_PERSON_WITH_NOT_VALID_PASSWORD);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("password must be not empty")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getAll_shouldReturnPageOfPersons_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        personRepository.save(person);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.ALL_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.personDtoList.size()").value(1),
                jsonPath("$._embedded.personDtoList[0].email").value(person.getEmail()),
                jsonPath("$._embedded.personDtoList[0].enabled").value(person.getEnabled()),
                jsonPath("$._embedded.personDtoList[0].authorities[0]").value(
                    person.getAuthorities().iterator().next().getAuthority())
            );

        assertThat(personRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutPersonsReadScope() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.ALL_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read"})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutAdminRole() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL + TestUrls.ALL_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.PERSONS_URL + TestUrls.ALL_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    TestUrls.PERSONS_URL + TestUrls.ALL_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getByEmail_shouldReturnPerson_whenCalledGetByEmailEndpointWithEmailThatExist() throws Exception {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);
        personRepository.save(person);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.email").value(person.getEmail()),
                jsonPath("$.enabled").value(person.getEnabled()),
                jsonPath("$.authorities[0]").value(person.getAuthorities().iterator().next().getAuthority())
            );

        assertThat(personRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getByEmail_shouldReturn403_whenCalledGetByEmailEndpointWithoutPersonsReadScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read"})
    void getByEmail_shouldReturn403_whenCalledGetByEmailEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getByEmail_shouldReturn400_whenCalledGetByEmailEndpointWithNotValidEmail() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL_WITH_NOT_VALID_EMAIL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("email not valid")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.read", "ROLE_admin"})
    void getByEmail_shouldReturn404_whenCalledGetByEmailEndpointWithEmailThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);
        var person = TestEntityFactory.createDefaultPerson();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("User with email: " + person.getEmail() + " not found")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void update_shouldReturn200_whenCalledUpdateEndpointWithEmailThatExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON);
        var updatePerson = TestEntityFactory.createUpdatePerson();
        var person = TestEntityFactory.createDefaultPerson();
        personRepository.save(person);

        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk());

        assertThat(personRepository.count()).isEqualTo(1);
        var updatedPerson = personRepository.findByEmail(person.getEmail()).get();
        assertAll(
            () -> assertThat(updatedPerson.getEnabled()).isEqualTo(updatedPerson.getEnabled()),
            () -> assertThat(updatedPerson.getAuthorities().iterator().next().getAuthority()).isEqualTo(
                updatePerson.getAuthorities().iterator().next())
        );
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutPersonsWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write"})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void update_shouldReturn400_whenCalledUpdateEndpointWithNotValidEmail() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON_WITH_NOT_VALID_EMAIL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("email not valid")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void update_shouldReturn400_whenCalledUpdateEndpointWithEnabledIsNull() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON_WITH_ENABLED_IS_NULL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("enabled must not be null")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void update_shouldReturn400_whenCalledUpdateEndpointWithAuthoritiesIsNull() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON_WITH_AUTHORITIES_IS_NULL);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("authorities must not be null")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void update_shouldReturn404_whenCalledUpdateEndpointWithEmailThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_PERSON);
        var person = TestEntityFactory.createDefaultPerson();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("User with email: " + person.getEmail() + " not found")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = "user@mail.com", authorities = {"SCOPE_persons.write"})
    void changePassword_shouldReturn200_whenCalledChangePasswordEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD);
        var personPassword = TestEntityFactory.createDefaultChangePersonPassword();
        var person = TestEntityFactory.createDefaultPerson();
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk());

        var changedPerson = personRepository.findByEmail(person.getEmail()).get();
        assertAll(
            () -> assertThat(personRepository.count()).isEqualTo(1),
            () -> assertThat(
                passwordEncoder.matches(personPassword.getNewPassword(), changedPerson.getPassword())).isTrue()
        );
    }

    @Test
    @WithMockUser(username = "user@mail.com", authorities = {"SCOPE_persons.read"})
    void changePassword_shouldReturn403_whenCalledChangePasswordEndpointWithoutPersonsReadScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = "user@mail.com", authorities = {"SCOPE_persons.write"})
    void changePassword_shouldReturn400_whenCalledChangePasswordEndpointWithNotValidOldPassword() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD_WITH_NOT_VALID_OLD_PASSWORD);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Old password can't be empty")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = "user@mail.com", authorities = {"SCOPE_persons.write"})
    void changePassword_shouldReturn400_whenCalledChangePasswordEndpointWithNotValidNewPassword() throws Exception {
        // GIVEN
        var jsonNode =
            jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD_WITH_NOT_VALID_NEW_PASSWORD);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("New password can't be empty")
            );

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = "admin@mail.com", authorities = {"SCOPE_persons.write"})
    void changePassword_shouldReturn404_whenCalledChangePasswordEndpointWithEmailThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD);
        var person = TestEntityFactory.createDefaultPerson();
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("User with email: admin@mail.com not found")
            );

        assertThat(personRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "user@mail.com", authorities = {"SCOPE_persons.write"})
    void changePassword_shouldReturn400_whenCalledChangePasswordEndpointWithIncorrectOldPassword() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CHANGE_PERSON_PASSWORD);
        var person = TestEntityFactory.createDefaultPerson();
        person.setPassword(passwordEncoder.encode("pass"));
        personRepository.save(person);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.PERSONS_URL + TestUrls.CHANGE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Old password is incorrect")
            );

        assertThat(personRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void delete_shouldReturn200_whenCalledDeleteEndpointWithEmailThatExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);
        var person = TestEntityFactory.createDefaultPerson();
        personRepository.save(person);

        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutPersonsWriteScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write"})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.PERSONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_persons.write", "ROLE_admin"})
    void delete_shouldReturn400_whenCalledDeleteEndpointWithNotValidEmail() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_PERSON_EMAIL_WITH_NOT_VALID_EMAIL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.PERSONS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("email not valid")
            );

        assertThat(personRepository.count()).isZero();
    }
}
