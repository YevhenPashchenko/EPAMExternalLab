package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.service.PersonService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<PersonDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return personService.getAllPersons(page, size);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<PersonDto> getById(@PathVariable("id") Long id) {
        return personService.getPersonDtoById(id);
    }
}
