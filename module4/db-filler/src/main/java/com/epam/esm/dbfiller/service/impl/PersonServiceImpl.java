package com.epam.esm.dbfiller.service.impl;

import com.epam.esm.dbfiller.entity.authorization.Person;
import com.epam.esm.dbfiller.repository.authorization.PersonRepository;
import com.epam.esm.dbfiller.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public void createPersons(List<Person> persons) {
        personRepository.saveAll(persons);
    }
}
