package com.epam.esm.authorization.integration.controller;

import com.epam.esm.authorization.AuthorizationApplication;
import com.epam.esm.authorization.dto.UpdateClientScopeDto;
import com.epam.esm.authorization.integration.constant.Authorities;
import com.epam.esm.authorization.integration.constant.TestFilePaths;
import com.epam.esm.authorization.integration.constant.TestUrls;
import com.epam.esm.authorization.integration.container.PostgreSqlTestContainer;
import com.epam.esm.authorization.integration.reader.JsonReader;
import com.epam.esm.authorization.repository.ClientRepository;
import com.epam.esm.authorization.repository.ClientScopeRepository;
import com.epam.esm.authorization.service.AuthoritiesService;
import com.epam.esm.authorization.service.ClientService;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(PostgreSqlTestContainer.class)
@SpringBootTest(classes = AuthorizationApplication.class)
class ClientControllerIntegrationTest {

    private static final String SCOPE = "scope";
    private static final String NEW_SCOPE = "clients.new";
    private final JsonReader jsonReader = new JsonReader();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private ClientScopeRepository clientScopeRepository;
    @Autowired
    private ClientService clientService;

    @BeforeEach
    public void clear() {
        clientRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser
    void create_shouldReturn403_whenCalledCreateEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonNode.toString())).andExpect(status().isForbidden());

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void create_shouldReturn404_whenCalledCreateEndpointWithScopeThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_WITH_SCOPE_THAT_NOT_EXIST);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Scope " + SCOPE + " not found")
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void createScope_shouldReturnCreatedClientScope_whenCalledCreateScopeEndpointWithValidBody() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.scope").value(SCOPE)
            );

        assertThat(authoritiesService.getAllScopes()).hasSize(5);
        authoritiesService.deleteScope(SCOPE);
    }

    @Test
    @WithMockUser
    void createScope_shouldReturn403_whenCalledCreateScopeEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(authoritiesService.getAllScopes()).hasSize(4);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void createScope_shouldReturn400_whenCalledCreateScopeEndpointWithEmptyScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_SCOPE_WITH_EMPTY_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("client scope must be not empty")
            );

        assertThat(authoritiesService.getAllScopes()).hasSize(4);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_READ})
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
                jsonPath("$._embedded.clientDtoList[0].scopes[0]").value(
                    client.getClientScopes().iterator().next().getScope())
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutClientsReadScope() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.CLIENTS_READ})
    void getAll_shouldReturn403_whenCalledGetAllEndpointWithoutAdminRole() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.VALID_PAGINATION_URL))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_READ})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_READ})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_READ})
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
                jsonPath("$.scopes[0]").value(client.getClientScopes().iterator().next().getScope())
            );

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void getByClientId_shouldReturn403_whenCalledGetByClientIdEndpointWithoutClientsReadScope() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.CLIENTS_READ})
    void getByClientId_shouldReturn403_whenCalledGetByClientIdEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_READ})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void getAllScopes_shouldReturnListOfClientScopes_whenCalledGetAllScopesEndpoint() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.size()").value(authoritiesService.getAllScopes().size())
            );
    }

    @Test
    @WithMockUser
    void getAllScopes_shouldReturn403_whenCalledGetAllScopesEndpointWithoutAdminRole() throws Exception {
        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.get(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_WRITE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
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
    @WithMockUser(authorities = {Authorities.CLIENTS_WRITE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_WRITE})
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
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_WRITE})
    void update_shouldReturn404_whenCalledUpdateEndpointWithClientScopeThatNotExist() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_WITH_CLIENT_SCOPE_THAT_NOT_EXIST);

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + "/" + client.getClientId())
                    .contentType(MediaType.APPLICATION_JSON).content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Scope " + SCOPE + " not found")
            );
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void updateScope_shouldReturnUpdatedClientScope_whenCalledUpdateScopeEndpointWithValidBody()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_SCOPE);
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.scope").value(NEW_SCOPE)
            );

        var scope = clientScopeRepository.findAll().get(0).getScope();
        assertAll(
            () -> assertThat(authoritiesService.getAllScopes()).hasSize(4),
            () -> assertThat(scope).isEqualTo(NEW_SCOPE)
        );

        var updateScope = new UpdateClientScopeDto();
        updateScope.setOldScope(NEW_SCOPE);
        updateScope.setNewScope(client.getClientScopes().iterator().next().getScope());
        clientService.updateScope(updateScope);
    }

    @Test
    @WithMockUser
    void updateScope_shouldReturn403_whenCalledUpdateScopeEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(authoritiesService.getAllScopes()).doesNotContain(NEW_SCOPE);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void updateScope_shouldReturn400_whenCalledUpdateScopeEndpointWithEmptyOldScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_SCOPE_WITH_EMPTY_OLD_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("client scope must be not empty")
            );

        assertThat(authoritiesService.getAllScopes()).doesNotContain(NEW_SCOPE);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void updateScope_shouldReturn400_whenCalledUpdateScopeEndpointWithEmptyNewScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_SCOPE_WITH_EMPTY_NEW_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("new client scope must be not empty")
            );

        assertThat(authoritiesService.getAllScopes()).doesNotContain(NEW_SCOPE);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void updateScope_shouldReturn404_whenCalledUpdateScopeEndpointWithOldScopeThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_UPDATE_CLIENT_SCOPE_WITH_OLD_SCOPE_THAT_NOT_EXIST);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.patch(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Scope " + SCOPE + " not found")
            );

        assertThat(authoritiesService.getAllScopes()).doesNotContain(NEW_SCOPE);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_WRITE})
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
                jsonPath("$.scopes[0]").value(client.getClientScopes().iterator().next().getScope())
            );

        assertThat(clientRepository.count()).isZero();
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutClientsWriteScope() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.CLIENTS_WRITE})
    void delete_shouldReturn403_whenCalledDeleteEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + "/" + client.getClientId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE, Authorities.CLIENTS_WRITE})
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

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void deleteScope_shouldReturnDeletedClientScope_whenCalledDeleteScopeEndpointWithValidBody()
        throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_DELETE_CLIENT_SCOPE);
        var client = TestEntityFactory.createDefaultClient();
        clientRepository.save(client);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.scope").value(client.getClientScopes().iterator().next().getScope())
            );

        var scopes = clientScopeRepository.findScopes();
        assertAll(
            () -> assertThat(authoritiesService.getAllScopes()).hasSize(3),
            () -> assertThat(scopes).isEmpty()
        );

        authoritiesService.addScope(client.getClientScopes().iterator().next().getScope());
    }

    @Test
    @WithMockUser
    void deleteScope_shouldReturn403_whenCalledDeleteScopeEndpointWithoutAdminRole() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_DELETE_CLIENT_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isForbidden());

        assertThat(authoritiesService.getAllScopes()).hasSize(4);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void deleteScope_shouldReturn400_whenCalledDeleteScopeEndpointWithEmptyScope() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_SCOPE_WITH_EMPTY_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.code").value(400),
                jsonPath("$.message").value("client scope must be not empty")
            );

        assertThat(authoritiesService.getAllScopes()).hasSize(4);
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN_ROLE})
    void deleteScope_shouldReturn404_whenCalledDeleteScopeEndpointWithScopeThatNotExist() throws Exception {
        // GIVEN
        var jsonNode = jsonReader.readJsonFile(TestFilePaths.PATH_TO_CREATE_CLIENT_SCOPE);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUrls.CLIENTS_URL + TestUrls.SCOPE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonNode.toString()))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.code").value(404),
                jsonPath("$.message").value("Scope " + SCOPE + " not found")
            );

        assertThat(authoritiesService.getAllScopes()).hasSize(4);
    }
}
