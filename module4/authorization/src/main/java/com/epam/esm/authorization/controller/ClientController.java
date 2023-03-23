package com.epam.esm.authorization.controller;

import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final AuthorizationServerSettings authorizationServerSettings;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody ClientDto client) {
        clientService.save(RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(client.getClientId())
            .clientSecret("{noop}" + client.getClientSecret())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri(authorizationServerSettings.getIssuer() + "/login")
            .scopes(scopes -> scopes.addAll(client.getScopes()))
            .build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_admin') && hasAuthority('SCOPE_clients.read')")
    public PagedModel<ClientDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return clientService.getAllClients(page, size);
    }

    @PreAuthorize("hasRole('ROLE_admin') && hasAuthority('SCOPE_clients.read')")
    @GetMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> getByClientId(@PathVariable String clientId) {
        return clientService.getClientById(clientId);
    }

    @PreAuthorize("hasRole('ROLE_admin') && hasAuthority('SCOPE_clients.write')")
    @PatchMapping(value = "/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> update(@PathVariable String clientId, @RequestBody UpdateClientDto client) {
        return clientService.update(clientId, client);
    }

    @PreAuthorize("hasRole('ROLE_admin') && hasAuthority('SCOPE_clients.write')")
    @DeleteMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> delete(@PathVariable String clientId) {
        return clientService.delete(clientId);
    }
}
