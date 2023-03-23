package com.epam.esm.authorization.assembler;

import com.epam.esm.authorization.controller.PersonController;
import com.epam.esm.authorization.dto.PersonDto;
import com.epam.esm.authorization.dto.PersonEmailDto;
import com.epam.esm.authorization.entity.Person;
import com.epam.esm.authorization.entity.PersonAuthority;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonDtoAssembler extends RepresentationModelAssemblerSupport<Person, PersonDto> {

    public PersonDtoAssembler() {
        super(PersonController.class, PersonDto.class);
    }

    @NonNull
    @Override
    public PersonDto toModel(@NonNull Person person) {
        var personDto = new PersonDto();
        personDto.setEmail(person.getEmail());
        personDto.setEnabled(person.getEnabled());
        personDto.setAuthorities(person.getAuthorities().stream()
            .map(PersonAuthority::getAuthority)
            .collect(Collectors.toSet()));

        var personEmailDto = new PersonEmailDto();
        personEmailDto.setEmail(person.getEmail());

        return personDto.add(linkTo(methodOn(PersonController.class).getByEmail(personEmailDto)).withSelfRel());
    }
}
