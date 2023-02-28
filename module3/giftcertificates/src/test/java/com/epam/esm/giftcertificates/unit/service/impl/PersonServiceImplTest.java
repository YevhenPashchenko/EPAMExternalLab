package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.PersonDtoAssembler;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.repository.PersonRepository;
import com.epam.esm.giftcertificates.service.PersonService;
import com.epam.esm.giftcertificates.service.impl.PersonServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class PersonServiceImplTest {

    private final PersonRepository personRepository = mock(PersonRepository.class);
    private final PersonDtoAssembler personDtoAssembler = mock(PersonDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Person> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final PersonService personService = new PersonServiceImpl(personRepository, personDtoAssembler,
        pagedResourcesAssembler);

    @Test
    void getAllPersons_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        personService.getAllPersons(0, 2);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(personRepository.findAll(PageRequest.of(0, 2)), personDtoAssembler);
    }

    @Test
    void getPersonDtoById_shouldReturnPerson_whenPersonWithThisIdExist() {
        // GIVEN
        var personDto = TestEntityFactory.createDefaultPersonDto();
        var person = TestEntityFactory.createDefaultPerson();
        given(personRepository.findById(anyLong())).willReturn(Optional.of(person));
        given(personDtoAssembler.toModel(any(Person.class))).willReturn(personDto);

        // WHEN
        var result = personService.getPersonDtoById(0L);

        // THEN
        assertThat(EntityModel.of(personDto)).isEqualTo(result);
    }

    @Test
    void getPersonDtoById_shouldThrowEntityNotFoundException_whenPersonWithThisIdNotExist() {
        // GIVEN
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> personService.getPersonDtoById(0L));
    }

    @Test
    void getPersonById_shouldReturnPerson_whenPersonWithThisIdExist() {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        given(personRepository.findById(anyLong())).willReturn(Optional.of(person));

        // WHEN
        var result = personService.getPersonById(0L);

        // THEN
        assertThat(person).isEqualTo(result);
    }

    @Test
    void getPersonById_shouldThrowEntityNotFoundException_whenPersonWithThisIdNotExist() {
        // GIVEN
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> personService.getPersonById(0L));
    }
}
