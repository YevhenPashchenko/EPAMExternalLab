package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.PersonController;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonDtoAssembler extends RepresentationModelAssemblerSupport<Person, PersonDto> {

    private final EntityDtoMapper entityDtoMapper;

    public PersonDtoAssembler(EntityDtoMapper entityDtoMapper) {
        super(PersonController.class, PersonDto.class);
        this.entityDtoMapper = entityDtoMapper;
    }

    @Override
    @NonNull
    public PersonDto toModel(@NonNull Person person) {
        var personDto = entityDtoMapper.personToPersonDto(person);
        addSelfLinkToPerson(personDto);
        return personDto;
    }

    private void addSelfLinkToPerson(PersonDto personDto) {
        personDto.add(linkTo(methodOn(PersonController.class).getById(personDto.getId())).withSelfRel());
    }
}
