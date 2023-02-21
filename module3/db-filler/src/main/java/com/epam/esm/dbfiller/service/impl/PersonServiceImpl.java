package com.epam.esm.dbfiller.service.impl;

import com.epam.esm.dbfiller.repository.PersonRepository;
import com.epam.esm.dbfiller.entity.Person;
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

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
