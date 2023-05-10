package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.assembler.ClientDtoAssembler;
import com.epam.esm.authorization.entity.Client;
import com.epam.esm.authorization.handler.exception.EntityNotFoundException;
import com.epam.esm.authorization.mapper.EntityDtoMapper;
import com.epam.esm.authorization.repository.ClientRepository;
import com.epam.esm.authorization.service.AuthoritiesService;
import com.epam.esm.authorization.service.impl.JpaRegisteredClientRepositoryService;
import com.epam.esm.authorization.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class JpaRegisteredClientRepositoryServiceTest {

    private static final String SCOPE = "clients.read";
    private static final String UPDATE_SCOPE = "clients.write";
    private static final int PAGE = 0;
    private static final int SIZE = 2;

    private final ClientRepository clientRepository = mock(ClientRepository.class);
    private final ClientDtoAssembler clientDtoAssembler = mock(ClientDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Client> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final AuthoritiesService authoritiesService = mock(AuthoritiesService.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final AuthorizationServerSettings authorizationServerSettings =
        AuthorizationServerSettings.builder().build();
    private final JpaRegisteredClientRepositoryService jpaRegisteredClientRepositoryService =
        new JpaRegisteredClientRepositoryService(clientRepository, clientDtoAssembler, pagedResourcesAssembler,
            authoritiesService, entityDtoMapper, authorizationServerSettings);

    @Test
    void create_shouldCallsClientRepositorySave_whenExecutedNormally() {
        // GIVEN
        given(authoritiesService.getAllScopes()).willReturn(Set.of(SCOPE));

        // WHEN
        jpaRegisteredClientRepositoryService.create(TestEntityFactory.createDefaultClientDto());

        // THEN
        then(clientRepository).should(atLeastOnce()).save(any(Client.class));
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenClientScopeNotExist() {
        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.create(TestEntityFactory.createDefaultClientDto()));
    }

    @Test
    void createScope_shouldReturnCreatedClientScope_whenExecutedNormally() {
        // GIVEN
        var clientScope = TestEntityFactory.createDefaultClientScopeDto();

        // WHEN
        var result = jpaRegisteredClientRepositoryService.createScope(clientScope);

        // THEN
        assertThat(result).isEqualTo(clientScope);
    }

    @Test
    void save_shouldCallsClientRepositorySave_whenExecutedNormally() {
        // WHEN
        jpaRegisteredClientRepositoryService.save(TestEntityFactory.createDefaultRegisteredClient());

        // THEN
        then(clientRepository).should(atLeastOnce()).save(any(Client.class));
    }

    @Test
    void getAllClients_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        jpaRegisteredClientRepositoryService.getAllClients(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(clientRepository.findAll(PageRequest.of(PAGE, SIZE)), clientDtoAssembler);
    }

    @Test
    void getClientById_shouldReturnClient_whenClientWithThisClientIdExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var clientDto = TestEntityFactory.createDefaultClientDto();
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.of(client));
        given(clientDtoAssembler.toModel(any(Client.class))).willReturn(clientDto);

        // WHEN
        var result = jpaRegisteredClientRepositoryService.getClientById(client.getClientId());

        // THEN
        assertThat(result).isEqualTo(EntityModel.of(clientDto));
    }

    @Test
    void getClientById_shouldThrowEntityNotFoundException_whenClientWithThisClientIdNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.getClientById(client.getClientId()));
    }

    @Test
    void getAllScopes_shouldReturnListClientScopes_whenExecutedNormally() {
        // GIVEN
        given(authoritiesService.getAllScopes()).willReturn(Set.of(SCOPE));

        // WHEN
        var result = jpaRegisteredClientRepositoryService.getAllScopes();

        // THEN
        assertThat(result).isEqualTo(List.of(TestEntityFactory.createDefaultClientScopeDto()));
    }

    @Test
    void findById_shouldReturnRegisteredClient_whenClientWithThisIdExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        given(clientRepository.findById(anyString())).willReturn(Optional.of(client));

        // WHEN
        var result = jpaRegisteredClientRepositoryService.findById(client.getId());

        // THEN
        assertAll(
            () -> assertThat(Objects.requireNonNull(result).getId()).isEqualTo(client.getId()),
            () -> assertThat(Objects.requireNonNull(result).getClientId()).isEqualTo(client.getClientId())
        );
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenClientWithThisIdNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        given(clientRepository.findById(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.findById(client.getId()));
    }

    @Test
    void findByClientId_shouldReturnRegisteredClient_whenClientWithThisClientIdExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.of(client));

        // WHEN
        var result = jpaRegisteredClientRepositoryService.findByClientId(client.getClientId());

        // THEN
        assertAll(
            () -> assertThat(Objects.requireNonNull(result).getId()).isEqualTo(client.getId()),
            () -> assertThat(Objects.requireNonNull(result).getClientId()).isEqualTo(client.getClientId())
        );
    }

    @Test
    void findByClientId_shouldThrowEntityNotFoundException_whenClientWithThisClientIdNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.findByClientId(client.getClientId()));
    }

    @Test
    void update_shouldReturnClient_whenClientWithThisClientIdExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var updateClient = TestEntityFactory.createDefaultUpdateClientDto();
        var clientDto = TestEntityFactory.createDefaultClientDto();
        given(authoritiesService.getAllScopes()).willReturn(Set.of(UPDATE_SCOPE));
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.of(client));
        given(clientDtoAssembler.toModel(client)).willReturn(clientDto);

        // WHEN
        var result = jpaRegisteredClientRepositoryService.update(client.getClientId(), updateClient);

        // THEN
        assertThat(result).isEqualTo(EntityModel.of(clientDto));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenClientScopeNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var updateClient = TestEntityFactory.createDefaultUpdateClientDto();

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.update(client.getClientId(), updateClient));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenClientWithThisClientIdNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var updateClient = TestEntityFactory.createDefaultUpdateClientDto();
        given(clientRepository.findByClientId(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.update(client.getClientId(), updateClient));
    }

    @Test
    void updateScope_shouldReturnUpdatedClientScope_whenExecutedNormally() {
        // GIVEN
        var clientScope = TestEntityFactory.createDefaultClientScopeDto();
        clientScope.setScope(UPDATE_SCOPE);
        given(authoritiesService.getAllScopes()).willReturn(Set.of(SCOPE));

        // WHEN
        var result =
            jpaRegisteredClientRepositoryService.updateScope(TestEntityFactory.createDefaultUpdateClientScopeDto());

        // THEN
        assertThat(result).isEqualTo(clientScope);
    }

    @Test
    void updateScope_shouldThrowEntityNotFoundException_whenClientScopeNotExist() {
        // THEN
        assertThrows(EntityNotFoundException.class, () -> jpaRegisteredClientRepositoryService.updateScope(
            TestEntityFactory.createDefaultUpdateClientScopeDto()));
    }

    @Test
    void delete_shouldReturnClient_whenClientWithThisClientIdExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();
        var clientDto = TestEntityFactory.createDefaultClientDto();
        given(clientRepository.deleteByClientId(anyString())).willReturn(List.of(client));
        given(clientDtoAssembler.toModel(client)).willReturn(clientDto);

        // WHEN
        var result = jpaRegisteredClientRepositoryService.delete(client.getClientId());

        // THEN
        assertThat(result).isEqualTo(EntityModel.of(clientDto));
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenClientWithThisClientIdNotExist() {
        // GIVEN
        var client = TestEntityFactory.createDefaultClient();

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.delete(client.getClientId()));
    }

    @Test
    void deleteScope_shouldReturnDeletedClientScope_whenExecutedNormally() {
        // GIVEN
        var clientScope = TestEntityFactory.createDefaultClientScopeDto();
        given(authoritiesService.getAllScopes()).willReturn(Set.of(SCOPE));

        // WHEN
        var result = jpaRegisteredClientRepositoryService.deleteScope(clientScope);

        // THEN
        assertThat(result).isEqualTo(clientScope);
    }

    @Test
    void deleteScope_shouldThrowEntityNotFoundException_whenClientScopeNotExist() {
        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> jpaRegisteredClientRepositoryService.deleteScope(TestEntityFactory.createDefaultClientScopeDto()));
    }
}
