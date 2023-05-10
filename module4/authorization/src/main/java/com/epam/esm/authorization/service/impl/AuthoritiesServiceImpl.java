package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.service.AuthoritiesService;
import com.epam.esm.authorization.service.ClientScopeService;
import com.epam.esm.authorization.service.PersonAuthorityService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AuthoritiesServiceImpl implements AuthoritiesService {

    private static final String ADMIN_ROLE = "ROLE_admin";
    private static final String USER_ROLE = "ROLE_user";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String PERSONS_READ = "persons.read";
    private static final String PERSONS_WRITE = "persons.write";
    private static final String CLIENTS_READ = "clients.read";
    private static final String CLIENTS_WRITE = "clients.write";
    private final PersonAuthorityService personAuthorityService;
    private final ClientScopeService clientScopeService;
    private final Set<String> roles = new HashSet<>(Set.of(ADMIN_ROLE, USER_ROLE));
    private final Set<String> scopes = new HashSet<>(Set.of(PERSONS_READ, PERSONS_WRITE, CLIENTS_READ, CLIENTS_WRITE));

    public AuthoritiesServiceImpl(PersonAuthorityService personAuthorityService,
        ClientScopeService clientScopeService) {
        this.personAuthorityService = personAuthorityService;
        this.clientScopeService = clientScopeService;
        personAuthorityService.getRoles().stream().filter(Objects::nonNull).forEach(roles::add);
        clientScopeService.getScopes().stream().filter(Objects::nonNull).forEach(scopes::add);
    }

    @Override
    public List<String> getSuperRoles() {
        return List.of(ADMIN_ROLE);
    }

    @Override
    public String getPersonsReadScope() {
        return SCOPE_PREFIX + PERSONS_READ;
    }

    @Override
    public String getPersonsWriteScope() {
        return SCOPE_PREFIX + PERSONS_WRITE;
    }

    @Override
    public String getClientReadScope() {
        return SCOPE_PREFIX + CLIENTS_READ;
    }

    @Override
    public String getClientWriteScope() {
        return SCOPE_PREFIX + CLIENTS_WRITE;
    }

    @Override
    public void addScope(String scope) {
        scopes.add(scope);
    }

    @Override
    public Set<String> getAllScopes() {
        return Collections.unmodifiableSet(scopes);
    }

    @Override
    public void updateScope(String oldScope, String newScope) {
        clientScopeService.updateScope(oldScope, newScope);
        scopes.remove(oldScope);
        scopes.add(newScope);
    }

    @Override
    public void deleteScope(String scope) {
        clientScopeService.deleteScope(scope);
        scopes.remove(scope);
    }

    @Override
    public void addRole(String role) {
        roles.add(role);
    }

    @Override
    public Set<String> getAllRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public void updateRole(String oldRole, String newRole) {
        personAuthorityService.updateRole(oldRole, newRole);
        roles.remove(oldRole);
        roles.add(newRole);
    }

    @Override
    public void deleteRole(String role) {
        personAuthorityService.deleteRole(role);
        roles.remove(role);
    }
}
