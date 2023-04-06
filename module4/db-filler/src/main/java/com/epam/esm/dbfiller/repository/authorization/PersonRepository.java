package com.epam.esm.dbfiller.repository.authorization;

import com.epam.esm.dbfiller.entity.authorization.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CRUD operations with {@link Person} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

}
