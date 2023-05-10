package com.epam.esm.dbfiller.service;

import com.epam.esm.dbfiller.entity.authorization.Person;

import java.util.List;

/**
 * API provides methods for services {@link Person} objects data before CRUD operations with them.
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
}
