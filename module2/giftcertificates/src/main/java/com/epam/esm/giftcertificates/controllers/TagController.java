package com.epam.esm.giftcertificates.controllers;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody TagDto tagDTO) {
        tagService.create(tagDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDto> get() {
        return tagService.get();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto getById(@PathVariable("id") long id) {
        return tagService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        tagService.delete(id);
    }
}
