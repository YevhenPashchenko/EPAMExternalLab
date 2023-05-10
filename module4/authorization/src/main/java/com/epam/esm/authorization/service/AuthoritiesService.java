package com.epam.esm.authorization.service;

import com.epam.esm.authorization.entity.Client;
import com.epam.esm.authorization.entity.Person;

import java.util.List;
import java.util.Set;

/**
 * Provides methods for {@link Person} roles and {@link Client} scopes management.
 */
public interface AuthoritiesService {

    /**
     * Returns a list of {@link Person} roles that grant super permissions.
     *
     * @return a list of {@link Person} roles.
     */
    List<String> getSuperRoles();

    /**
     * Returns a permission that grant possibility read {@link Person} objects.
     *
     * @return scope value.
     */
    String getPersonsReadScope();

    /**
     * Returns a permission that grant possibility write {@link Person} objects.
     *
     * @return scope value.
     */
    String getPersonsWriteScope();

    /**
     * Returns a permission that grant possibility read {@link Client} objects.
     *
     * @return scope value.
     */
    String getClientReadScope();

    /**
     * Returns a permission that grant possibility write {@link Client} objects.
     *
     * @return scope value.
     */
    String getClientWriteScope();

    /**
     * Adds new scope permission.
     *
     * @param scope scope value.
     */
    void addScope(String scope);

    /**
     * Returns a set of {@link Client} scopes.
     *
     * @return set of {@link Client} scopes.
     */
    Set<String> getAllScopes();

    /**
     * Updates scope permission.
     *
     * @param oldScope old scope value.
     * @param newScope new scope value.
     */
    void updateScope(String oldScope, String newScope);

    /**
     * Deletes scope permission.
     *
     * @param scope scope value.
     */
    void deleteScope(String scope);

    /**
     * Adds new {@link Person} role.
     *
     * @param role {@code role}
     */
    void addRole(String role);

    /**
     * Returns a set of {@link Person} roles.
     *
     * @return set of {@link Person} roles.
     */
    Set<String> getAllRoles();

    /**
     * Updates {@link Person} role.
     *
     * @param oldRole old role value.
     * @param newRole new role value.
     */
    void updateRole(String oldRole, String newRole);

    /**
     * Deletes {@link Person} role.
     *
     * @param role role value.
     */
    void deleteRole(String role);
}
