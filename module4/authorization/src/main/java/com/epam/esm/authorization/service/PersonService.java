package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.PersonDto;
import com.epam.esm.authorization.dto.PersonRoleDto;
import com.epam.esm.authorization.dto.UpdatePersonRoleDto;
import com.epam.esm.authorization.entity.Person;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

/**
 * An extension of {@link UserDetailsManager} which provides the ability to get users.
 *
 * @author Yevhen Pashchenko
 */
public interface PersonService extends UserDetailsManager {

    /**
     * Creates a {@link Person} role.
     *
     * @param personRole {@link PersonRoleDto} object.
     * @return {@link PersonRoleDto} object.
     */
    PersonRoleDto createRole(PersonRoleDto personRole);

    /**
     * Returns a list of {@link PersonDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link PersonDto} objects.
     */
    PagedModel<PersonDto> getAllUsers(int page, int size);

    /**
     * Returns an {@link PersonDto} object by {@code email}.
     *
     * @param email {@code email}.
     * @return {@link PersonDto} object.
     */
    EntityModel<PersonDto> getUserByEmail(String email);

    /**
     * Returns a list of {@link PersonRoleDto} objects.
     *
     * @return list of {@link PersonRoleDto} objects.
     */
    List<PersonRoleDto> getAllRoles();

    /**
     * Updates {@link Person} role.
     *
     * @param updatePersonRole {@link UpdatePersonRoleDto} object.
     * @return {@link PersonRoleDto} objects.
     */
    PersonRoleDto updateRole(UpdatePersonRoleDto updatePersonRole);

    /**
     * Deletes {@link Person} role.
     *
     * @param personRole {@link PersonRoleDto} object.
     * @return {@link PersonRoleDto} object.
     */
    PersonRoleDto deleteRole(PersonRoleDto personRole);
}
