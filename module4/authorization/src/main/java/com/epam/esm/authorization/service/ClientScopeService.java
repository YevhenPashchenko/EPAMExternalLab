package com.epam.esm.authorization.service;

import com.epam.esm.authorization.entity.Client;

import java.util.Set;

/**
 * Provides methods for {@link Client} scopes management.
 */
public interface ClientScopeService {

    /**
     * Returns a set of {@link Client} scopes.
     *
     * @return a set of {@link Client} scopes.
     */
    Set<String> getScopes();

    /**
     * Updates a {@link Client} scope.
     *
     * @param oldScope old scope value.
     * @param newScope new scope value.
     */
    void updateScope(String oldScope, String newScope);

    /**
     * Deletes a {@link Client} scope.
     *
     * @param scope scope value.
     */
    void deleteScope(String scope);
}
