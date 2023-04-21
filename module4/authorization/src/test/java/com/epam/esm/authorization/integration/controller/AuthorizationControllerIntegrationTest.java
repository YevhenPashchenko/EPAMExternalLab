package com.epam.esm.authorization.integration.controller;

import com.epam.esm.authorization.AuthorizationApplication;
import com.epam.esm.authorization.integration.constant.Authorities;
import com.epam.esm.authorization.integration.constant.TestFilePaths;
import com.epam.esm.authorization.integration.constant.TestUrls;
import com.epam.esm.authorization.integration.container.PostgreSqlTestContainer;
import com.epam.esm.authorization.integration.reader.JsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = AuthorizationApplication.class)
class AuthorizationControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getToken_shouldReturn200_whenCalledLoginEndpointWithClientIdThatExist() throws Exception {
        // GIVEN
        var clientId = "base-client";

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.LOGIN_URL + "/" + clientId)).andExpect(status().isOk());
    }

    @Test
    void getToken_shouldReturn404_whenCalledLoginEndpointWithClientIdThatNotExist() throws Exception {
        // GIVEN
        var clientId = "client";

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get(TestUrls.LOGIN_URL + "/" + clientId))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value(
                    "Client with client id " + clientId + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn200_whenCalledLoginEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void login_shouldReturn403_whenCalledLoginEndpointWithoutPersonsReadScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn404_whenCalledLoginEndpointWithClientIdThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_WRONG_CLIENT_ID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value(
                    "Client with client id " + jsonNode.get("client-id").asText() + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithClientIdThatNotValid() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_CLIENT_ID_THAT_NOT_VALID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("client id must be not empty")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn404_whenCalledLoginEndpointWithEmailThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_WRONG_EMAIL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("User with email: " + jsonNode.get("email").asText() + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithEmailThatNotValid() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_EMAIL_THAT_NOT_VALID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("email not valid")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithIncorrectPassword() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_INCORRECT_PASSWORD);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Password is incorrect")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithPasswordThatNotValid() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_PASSWORD_THAT_NOT_VALID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("password must be not empty")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithNullRedirectUri() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_NULL_REDIRECT_URI);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("redirect uri must be not null")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.PERSONS_READ})
    void login_shouldReturn400_whenCalledLoginEndpointWithIncorrectScopeValue() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_LOGIN_WITH_INCORRECT_SCOPE_VALUE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("OAuth 2.0 Parameter: scope")
            );
    }
}
