package com.epam.esm.authorization.controller;

import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.ClientScopeDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.dto.UpdateClientScopeDto;
import com.epam.esm.authorization.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody ClientDto client) {
        clientService.create(client);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @PostMapping(value = "/scope", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientScopeDto createScope(@Valid @RequestBody ClientScopeDto clientScope) {
        return clientService.createScope(clientScope);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.clientReadScope)")
    public PagedModel<ClientDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return clientService.getAllClients(page, size);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.clientReadScope)")
    @GetMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> getByClientId(@PathVariable String clientId) {
        return clientService.getClientById(clientId);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @GetMapping(value = "/scope", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientScopeDto> getAllScopes() {
        return clientService.getAllScopes();
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.clientWriteScope)")
    @PatchMapping(value = "/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> update(@PathVariable String clientId, @RequestBody UpdateClientDto client) {
        return clientService.update(clientId, client);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @PatchMapping(value = "/scope", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientScopeDto updateScope(@Valid @RequestBody UpdateClientScopeDto updateClientScope) {
        return clientService.updateScope(updateClientScope);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.clientWriteScope)")
    @DeleteMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ClientDto> delete(@PathVariable String clientId) {
        return clientService.delete(clientId);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @DeleteMapping(value = "/scope", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientScopeDto deleteScope(@Valid @RequestBody ClientScopeDto clientScope) {
        return clientService.deleteScope(clientScope);
    }
}
