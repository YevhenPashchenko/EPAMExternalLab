package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.PersonDtoAssembler;
import com.epam.esm.giftcertificates.repository.PersonRepository;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.handler.exception.PersonNotFoundException;
import com.epam.esm.giftcertificates.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;
  private final PersonDtoAssembler personDtoAssembler;
  private final PagedResourcesAssembler<Person> pagedResourcesAssembler;

  @Override
  public PagedModel<PersonDto> getAllPersons(int page, int size) {
    return pagedResourcesAssembler.toModel(
        personRepository.findAll(PageRequest.of(page, size)), personDtoAssembler);
  }

  @Override
  public EntityModel<PersonDto> getPersonDtoById(Long id) {
    return EntityModel.of(personDtoAssembler.toModel(getPersonById(id)));
  }

  @Override
  public Person getPersonById(Long id) {
    return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
  }
}
