package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.entity.Client;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * An extension of {@link RegisteredClientRepository} which provides the ability to get, update and delete clients.
 *
 * @author Yevhen Pashchenko
 */
public interface ClientService extends RegisteredClientRepository {

    /**
     * Create a {@link RegisteredClient} object.
     *
     * @param client {@link ClientDto} object.
     */
    void create(ClientDto client);

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
     * Updates an {@link Client} object and returns it.
     *
     * @param clientId {@code clientId}.
     * @param client   {@link UpdateClientDto} object.
     * @return {@link ClientDto} object.
     */
    EntityModel<ClientDto> update(String clientId, UpdateClientDto client);

    /**
     * Deletes an {@link Client} object and returns it.
     *
     * @param clientId {@code clientId}.
     * @return {@link ClientDto} object.
     */
    EntityModel<ClientDto> delete(String clientId);
}
