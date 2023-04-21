package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.ClientScopeDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.dto.UpdateClientScopeDto;
import com.epam.esm.authorization.entity.Client;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;

/**
 * An extension of {@link RegisteredClientRepository} which provides the ability to get, update and delete clients.
 *
 * @author Yevhen Pashchenko
 */
public interface ClientService extends RegisteredClientRepository {

    /**
     * Creates a {@link RegisteredClient} object.
     *
     * @param client {@link ClientDto} object.
     */
    void create(ClientDto client);

    /**
     * Creates a {@link Client} scope.
     *
     * @param clientScope {@link ClientScopeDto} object.
     * @return {@link ClientScopeDto} object.
     */
    ClientScopeDto createScope(ClientScopeDto clientScope);

    /**
     * Returns a list of {@link ClientDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link ClientDto} objects.
     */
    PagedModel<ClientDto> getAllClients(int page, int size);

    /**
     * Returns an {@link ClientDto} object by {@code clientId}.
     *
     * @param clientId {@code clientId}.
     * @return {@link ClientDto} object.
     */
    EntityModel<ClientDto> getClientById(String clientId);

    /**
     * Returns a list of {@link ClientScopeDto} objects.
     *
     * @return list of {@link ClientScopeDto} objects.
     */
    List<ClientScopeDto> getAllScopes();

    /**
     * Updates an {@link Client} object and returns it.
     *
     * @param clientId {@code clientId}.
     * @param client   {@link UpdateClientDto} object.
     * @return {@link ClientDto} object.
     */
    EntityModel<ClientDto> update(String clientId, UpdateClientDto client);

    /**
     * Updates a {@link Client} scope.
     *
     * @param updateClientScope {@link UpdateClientScopeDto} object.
     * @return {@link ClientScopeDto} object.
     */
    ClientScopeDto updateScope(UpdateClientScopeDto updateClientScope);

    /**
     * Deletes an {@link Client} object and returns it.
     *
     * @param clientId {@code clientId}.
     * @return {@link ClientDto} object.
     */
    EntityModel<ClientDto> delete(String clientId);

    /**
     * Deletes a {@link Client} scope.
     * @param clientScope {@link ClientScopeDto} object.
     * @return {@link ClientScopeDto} object.
     */
    ClientScopeDto deleteScope(ClientScopeDto clientScope);
}
