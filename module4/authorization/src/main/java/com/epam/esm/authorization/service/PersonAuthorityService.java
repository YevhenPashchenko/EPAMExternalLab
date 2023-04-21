package com.epam.esm.authorization.service;

import com.epam.esm.authorization.entity.Person;

import java.util.Set;

/**
 * Provides methods for {@link Person} roles management.
 */
public interface PersonAuthorityService {

    /**
     * Returns a set of {@link Person} roles.
     *
     * @return a set of {@link Person} roles.
     */
    Set<String> getRoles();

    /**
     * Updates a {@link Person} role.
     *
     * @param oldRole old role value.
     * @param newRole new role value.
     */
    void updateRole(String oldRole, String newRole);

    /**
     * Deletes a {@link Person} role.
     *
     * @param role role value.
     */
    void deleteRole(String role);
}
