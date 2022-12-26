package com.epam.esm.giftcertificates.controllers;

import com.epam.esm.giftcertificates.entities.dto.TagDTO;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    private static final String JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping()
    public void create(@RequestBody TagDTO tagDTO) {
        tagService.create(tagDTO);
    }

    @GetMapping(produces = JSON_MEDIA_TYPE)
    public List<TagDTO> get() {
        return tagService.get();
    }

    @GetMapping(value = "/{id}", produces = JSON_MEDIA_TYPE)
    public TagDTO getById(@PathVariable("id") long id) {
        return tagService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        tagService.delete(id);
    }
}
