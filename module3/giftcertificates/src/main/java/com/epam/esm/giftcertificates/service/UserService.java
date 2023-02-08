package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.UserDto;
import com.epam.esm.giftcertificates.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link User} objects data before read operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface UserService {

  /**
   * Returns a list of {@link UserDto} objects from given {@code page} and {@code size}.
   *
   * @param page {@code page}.
   * @param size {@code size}.
   * @return list of {@link UserDto} objects.
   */
  PagedModel<UserDto> getAllUserDto(int page, int size);

  /**
   * Returns an {@link UserDto} object by {@link User} object {@code id}.
   *
   * @param id {@code id}.
   * @return {@link UserDto} object.
   */
  EntityModel<UserDto> getUserDtoById(Long id);

  /**
   * Returns an {@link User} object by {@code id}.
   *
   * @param id {@code id}.
   * @return {@link User} object.
   */
  User getUserById(Long id);
}
