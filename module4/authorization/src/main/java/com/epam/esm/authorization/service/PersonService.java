package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.PersonDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * An extension of {@link UserDetailsManager} which provides the ability to get users.
 *
 * @author Yevhen Pashchenko
 */
public interface PersonService extends UserDetailsManager {

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
}
