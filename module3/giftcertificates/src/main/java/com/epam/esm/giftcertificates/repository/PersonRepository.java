package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CRD operations with {@link Person} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
