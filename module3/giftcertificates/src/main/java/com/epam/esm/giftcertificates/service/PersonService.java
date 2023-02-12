package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.Person;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link Person} objects data before read operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface PersonService {

  /**
   * Returns a list of {@link PersonDto} objects from given {@code page} and {@code size}.
   *
   * @param page {@code page}.
   * @param size {@code size}.
   * @return list of {@link PersonDto} objects.
   */
  PagedModel<PersonDto> getAllPersons(int page, int size);

  /**
   * Returns an {@link PersonDto} object by {@link Person} object {@code id}.
   *
   * @param id {@code id}.
   * @return {@link PersonDto} object.
   */
  EntityModel<PersonDto> getPersonDtoById(Long id);

  /**
   * Returns an {@link Person} object by {@code id}.
   *
   * @param id {@code id}.
   * @return {@link Person} object.
   */
  Person getPersonById(Long id);
}
