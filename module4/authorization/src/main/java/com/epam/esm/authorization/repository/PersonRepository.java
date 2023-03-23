package com.epam.esm.authorization.repository;

import com.epam.esm.authorization.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}
