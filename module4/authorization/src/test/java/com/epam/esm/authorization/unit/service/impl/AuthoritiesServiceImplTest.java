package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.service.ClientScopeService;
import com.epam.esm.authorization.service.PersonAuthorityService;
import com.epam.esm.authorization.service.impl.AuthoritiesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AuthoritiesServiceImplTest {

    private static final String ADMIN_ROLE = "ROLE_admin";
    private static final String USER_ROLE = "ROLE_user";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String PERSONS_READ = "persons.read";
    private static final String PERSONS_WRITE = "persons.write";
    private static final String CLIENTS_READ = "clients.read";
    private static final String CLIENTS_WRITE = "clients.write";
    private static final String NEW_SCOPE = "scope";
    private static final String NEW_ROLE = "role";
    private final PersonAuthorityService personAuthorityService = mock(PersonAuthorityService.class);
    private final ClientScopeService clientScopeService = mock(ClientScopeService.class);
    private AuthoritiesServiceImpl authoritiesService;

    @BeforeEach
    public void init() {
        authoritiesService = new AuthoritiesServiceImpl(personAuthorityService, clientScopeService);
    }

    @Test
    void getSuperRoles_shouldReturnListRolesWithSuperPermissions_whenExecutedNormally() {
        // WHEN
        var result = authoritiesService.getSuperRoles();

        // THEN
        assertThat(result).isEqualTo(List.of(ADMIN_ROLE));
    }

    @Test
    void getPersonsReadScope_shouldReturnScopeThatGrantPermissionReadPerson_whenExecutedNormally() {
        // WHEN
        var result = authoritiesService.getPersonsReadScope();

        // THEN
        assertThat(result).isEqualTo(SCOPE_PREFIX + PERSONS_READ);
    }

    @Test
    void getPersonsWriteScope_shouldReturnScopeThatGrantPermissionReadPerson_whenExecutedNormally() {
        // WHEN
        var result = authoritiesService.getPersonsWriteScope();

        // THEN
        assertThat(result).isEqualTo(SCOPE_PREFIX + PERSONS_WRITE);
    }

    @Test
    void getClientReadScope_shouldReturnScopeThatGrantPermissionReadPerson_whenExecutedNormally() {
        // WHEN
        var result = authoritiesService.getClientReadScope();

        // THEN
        assertThat(result).isEqualTo(SCOPE_PREFIX + CLIENTS_READ);
    }

    @Test
    void getClientWriteScope_shouldReturnScopeThatGrantPermissionReadPerson_whenExecutedNormally() {
        // WHEN
        var result = authoritiesService.getClientWriteScope();

        // THEN
        assertThat(result).isEqualTo(SCOPE_PREFIX + CLIENTS_WRITE);
    }

    @Test
    void addScope_shouldAddScopeToSet_whenExecutedNormally() {
        // WHEN
        authoritiesService.addScope(NEW_SCOPE);

        // THEN
        assertThat(authoritiesService.getAllScopes()).contains(NEW_SCOPE);
    }

    @Test
    void getAllScopes_shouldReturnSetExistingScopes_whenExecutedNormally() {
        // GIVEN
        var scopes = new HashSet<>(Set.of(PERSONS_READ, PERSONS_WRITE, CLIENTS_READ, CLIENTS_WRITE));

        // WHEN
        var result = authoritiesService.getAllScopes();

        // THEN
        assertThat(result).isEqualTo(scopes);
    }

    @Test
    void updateScope_shouldChangeScope_whenExecutedNormally() {
        // WHEN
        authoritiesService.updateScope(PERSONS_READ, NEW_SCOPE);

        // THEN
        assertThat(authoritiesService.getAllScopes()).doesNotContain(PERSONS_READ);
        assertThat(authoritiesService.getAllScopes()).contains(NEW_SCOPE);
    }

    @Test
    void deleteScope_shouldDeleteScope_whenExecutedNormally() {
        // WHEN
        authoritiesService.deleteScope(PERSONS_READ);

        // THEN
        assertThat(authoritiesService.getAllScopes()).doesNotContain(PERSONS_READ);
    }

    @Test
    void addRole_shouldAddRoleToSet_whenExecutedNormally() {
        // WHEN
        authoritiesService.addRole(NEW_ROLE);

        // THEN
        assertThat(authoritiesService.getAllRoles()).contains(NEW_ROLE);
    }

    @Test
    void getAllRoles_shouldReturnSetExistingRoles_whenExecutedNormally() {
        // GIVEN
        var roles = new HashSet<>(Set.of(ADMIN_ROLE, USER_ROLE));

        // WHEN
        var result = authoritiesService.getAllRoles();

        // THEN
        assertThat(result).isEqualTo(roles);
    }

    @Test
    void updateRole_shouldChangeRole_whenExecutedNormally() {
        // WHEN
        authoritiesService.updateRole(USER_ROLE, NEW_ROLE);

        // THEN
        assertThat(authoritiesService.getAllRoles()).doesNotContain(USER_ROLE);
        assertThat(authoritiesService.getAllRoles()).contains(NEW_ROLE);
    }

    @Test
    void deleteRole_shouldDeleteRole_whenExecutedNormally() {
        // WHEN
        authoritiesService.deleteRole(USER_ROLE);

        // THEN
        assertThat(authoritiesService.getAllRoles()).doesNotContain(USER_ROLE);
    }
}
