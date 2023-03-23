package com.epam.esm.authorization.integration.controller;

import com.epam.esm.authorization.AuthorizationApplication;
import com.epam.esm.authorization.integration.constant.TestFilePaths;
import com.epam.esm.authorization.integration.constant.TestUrls;
import com.epam.esm.authorization.integration.container.PostgreSqlTestContainer;
import com.epam.esm.authorization.integration.reader.JsonReader;
import com.epam.esm.authorization.repository.ClientRepository;
import com.epam.esm.authorization.util.TestEntityFactory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = AuthorizationApplication.class)
class ClientControllerIntegrationTest {

    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void clear() {
        clientRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void create_shouldReturn200_whenCalledCreateEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isOk());

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"user"})
    void create_shouldReturn403_whenCalledCreateEndpointWithRoleUser() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isForbidden());

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void create_shouldReturn400_whenCalledCreateEndpointWithClientIdThatNotValid() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_WITH_CLIENT_ID_THAT_NOT_VALID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Client id can't be empty")
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void create_shouldReturn400_whenCalledCreateEndpointWithClientIdThatAlreadyExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT);
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("client_id=" + client.getClientId() + " already exists.")
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void create_shouldReturn400_whenCalledCreateEndpointWithClientSecretThatNotValid() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_WITH_CLIENT_SECRET_THAT_NOT_VALID);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Client secret can't be empty")
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void create_shouldReturn400_whenCalledCreateEndpointWithScopesThatIsNull() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_WITH_SCOPES_THAT_IS_NULL);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Scopes must not be null")
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read", "ROLE_admin"})
    void getAll_shouldReturnPageOfClients_whenCalledGetAllEndpointWithValidPagination() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$._embedded.clientDtoList.size()").value(1),
                jsonPath("$._embedded.clientDtoList[0].client-id").value(client.getClientId()),
                jsonPath("$._embedded.clientDtoList[0].client-secret").value(client.getClientSecret()),
                jsonPath("$._embedded.clientDtoList[0].scopes[0]").value(client.getScopes())
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutClientsReadScope() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read"})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutAdminRole() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read", "ROLE_admin"})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidPageOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.NOT_VALID_PAGE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page index must not be less than zero")
            );
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read", "ROLE_admin"})
    void getAll_shouldReturn400_whenCalledGetAllEndpointWithNotValidSizeOfPagination() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.NOT_VALID_SIZE_PAGINATION_URL))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("Page size must not be less than one")
            );
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read", "ROLE_admin"})
    void getByClientId_shouldReturnClient_whenCalledGetByClientIdEndpointWithClientIdThatExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.client-id").value(client.getClientId()),
                jsonPath("$.client-secret").value(client.getClientSecret()),
                jsonPath("$.scopes[0]").value(client.getScopes())
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getByClientId_shouldReturn403_whenCalledGetByClientIdEndpointWithoutClientsReadScope() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read"})
    void getByClientId_shouldReturn403_whenCalledGetByClientIdEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.read", "ROLE_admin"})
    void getByClientId_shouldReturn404_whenCalledGetByClientIdEndpointWithClientIdThatNotExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Client with client id " + client.getClientId() + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write", "ROLE_admin"})
    void update_shouldReturnUpdatedClient_whenCalledUpdateEndpointWithClientIdThatExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var updateClient = TestEntityFactory.createDefaultUpdateClientDto();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT);
        clientRepository.save(client);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + "/" + client.getClientId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.client-id").value(updateClient.getClientId()),
                jsonPath("$.client-secret").value(updateClient.getClientSecret()),
                jsonPath("$.scopes[0]").value(updateClient.getScopes().iterator().next())
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutClientsWriteScope() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + "/" + client.getClientId())
                    .contentType(MediaType.APPLICATION_JSON).content(jsonNode.toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write"})
    void update_shouldReturn403_whenCalledUpdateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + "/" + client.getClientId())
                    .contentType(MediaType.APPLICATION_JSON).content(jsonNode.toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write", "ROLE_admin"})
    void update_shouldReturn404_whenCalledUpdateEndpointWithClientIdThatNotExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + "/" + client.getClientId())
                    .contentType(MediaType.APPLICATION_JSON).content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Client with client id " + client.getClientId() + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write", "ROLE_admin"})
    void delete_shouldReturnClient_whenCalledDeleteEndpointWithClientIdThatExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.client-id").value(client.getClientId()),
                jsonPath("$.client-secret").value(client.getClientSecret()),
                jsonPath("$.scopes[0]").value(client.getScopes())
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutClientsWriteScope() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write"})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_clients.write", "ROLE_admin"})
    void delete_shouldReturn404_whenCalledDeleteEndpointWithClientIdThatNotExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Client with client id " + client.getClientId() + " not found")
            );
    }
}
