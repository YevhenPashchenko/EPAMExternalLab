package com.epam.esm.dbfiller.service;

import com.epam.esm.dbfiller.entity.Person;

import java.util.List;

/**
 * API provides methods for services {@link Person} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface PersonService {

    /**
     * Saves list of {@link Person} objects.
     *
     * @param persons list of {@link Person} objects.
     */
    void createPersons(List<Person> persons);

    /**
     * Returns an {@link Person} object by {@code id}.
     *
     * @param id {@code id}.
     * @return {@link Person} object.
     */
    Person getPersonById(Long id);
}
