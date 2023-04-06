package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.assembler.ClientDtoAssembler;
import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.entity.Client;
import com.epam.esm.authorization.handler.exception.EntityNotFoundException;
import com.epam.esm.authorization.mapper.EntityDtoMapper;
import com.epam.esm.authorization.repository.ClientRepository;
import com.epam.esm.authorization.service.ClientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class JpaRegisteredClientRepositoryService implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientDtoAssembler clientDtoAssembler;
    private final PagedResourcesAssembler<Client> pagedResourcesAssembler;
    private final EntityDtoMapper entityDtoMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthorizationServerSettings authorizationServerSettings;

    public JpaRegisteredClientRepositoryService(ClientRepository clientRepository,
        ClientDtoAssembler clientDtoAssembler, PagedResourcesAssembler<Client> pagedResourcesAssembler,
        EntityDtoMapper entityDtoMapper, AuthorizationServerSettings authorizationServerSettings) {
        this.clientRepository = clientRepository;
        this.clientDtoAssembler = clientDtoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.entityDtoMapper = entityDtoMapper;
        this.authorizationServerSettings = authorizationServerSettings;

        var classLoader = JpaRegisteredClientRepositoryService.class.getClassLoader();
        var securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    @Override
    public void create(ClientDto client) {
        save(RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(client.getClientId())
            .clientSecret("{noop}" + client.getClientSecret())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri(authorizationServerSettings.getIssuer() + "/login")
            .scopes(scopes -> scopes.addAll(client.getScopes()))
            .build());
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        clientRepository.save(toEntity(registeredClient));
    }

    private Client toEntity(RegisteredClient registeredClient) {
        var entity = new Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(getClientIdIssuedAt(registeredClient.getClientIdIssuedAt()));
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(
            getClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods()));
        entity.setAuthorizationGrantTypes(getAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes()));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    private Instant getClientIdIssuedAt(Instant clientIdIssuedAt) {
        return clientIdIssuedAt != null ? clientIdIssuedAt : Instant.now();
    }

    private String getClientAuthenticationMethods(Set<ClientAuthenticationMethod> methods) {
        var listOfMethods = new ArrayList<String>(methods.size());
        methods.forEach(clientAuthenticationMethod -> listOfMethods.add(clientAuthenticationMethod.getValue()));
        return StringUtils.collectionToCommaDelimitedString(listOfMethods);
    }

    private String getAuthorizationGrantTypes(Set<AuthorizationGrantType> authorizationGrantTypes) {
        var listOfGrantTypes = new ArrayList<String>(authorizationGrantTypes.size());
        authorizationGrantTypes.forEach(
            authorizationGrantType -> listOfGrantTypes.add(authorizationGrantType.getValue()));
        return StringUtils.collectionToCommaDelimitedString(listOfGrantTypes);
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
    }

    @Override
    public PagedModel<ClientDto> getAllClients(int page, int size) {
        return pagedResourcesAssembler.toModel(clientRepository.findAll(PageRequest.of(page, size)),
            clientDtoAssembler);
    }

    @Override
    public EntityModel<ClientDto> getClientById(String clientId) {
        return EntityModel.of(clientDtoAssembler.toModel(
            clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException(setErrorMessage(clientId)))));
    }

    private String setErrorMessage(String value) {
        return "Client with client id " + value + " not found";
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return clientRepository.findById(id).map(this::toRegisteredClient)
            .orElseThrow(() -> new EntityNotFoundException("Client with id " + id + " not found"));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return clientRepository.findByClientId(clientId)
            .map(this::toRegisteredClient)
            .orElseThrow(() -> new EntityNotFoundException(setErrorMessage(clientId)));
    }

    private RegisteredClient toRegisteredClient(Client client) {
        return RegisteredClient.withId(client.getId())
            .clientId(client.getClientId())
            .clientIdIssuedAt(client.getClientIdIssuedAt())
            .clientSecret(client.getClientSecret())
            .clientSecretExpiresAt(client.getClientSecretExpiresAt())
            .clientName(client.getClientName())
            .clientAuthenticationMethods(authenticationMethods ->
                StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods())
                    .forEach(authenticationMethod -> authenticationMethods.add(
                        resolveClientAuthenticationMethod(authenticationMethod))))
            .authorizationGrantTypes(authorizationGrantTypes ->
                StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes())
                    .forEach(grantType -> authorizationGrantTypes.add(resolveAuthorizationGrantType(grantType))))
            .redirectUris(
                uris -> uris.addAll(StringUtils.commaDelimitedListToSet(client.getRedirectUris())))
            .scopes(scopes -> scopes.addAll(StringUtils.commaDelimitedListToSet(client.getScopes())))
            .clientSettings(ClientSettings.withSettings(parseMap(client.getClientSettings())).build())
            .tokenSettings(getTokenSettings(client.getTokenSettings()))
            .build();
    }

    private ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    private AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
    }

    private TokenSettings getTokenSettings(String tokenSettings) {
        var tokenSettingsMap = parseMap(tokenSettings);
        var settings = TokenSettings.withSettings(tokenSettingsMap);
        if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            settings.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        return settings.build();
    }

    @Override
    @Transactional
    public EntityModel<ClientDto> update(String clientId, UpdateClientDto updateClient) {
        var client = clientRepository.findByClientId(clientId)
            .orElseThrow(() -> new EntityNotFoundException(setErrorMessage(clientId)));
        entityDtoMapper.updateClient(client, updateClient);
        return EntityModel.of(clientDtoAssembler.toModel(client));
    }

    @Override
    @Transactional
    public EntityModel<ClientDto> delete(String clientId) {
        return EntityModel.of(
            clientDtoAssembler.toModel(clientRepository.deleteByClientId(clientId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(setErrorMessage(clientId)))));
    }
}
