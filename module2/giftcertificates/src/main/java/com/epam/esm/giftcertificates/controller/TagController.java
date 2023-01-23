package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.service.TagService;
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
    public void createTag(@RequestBody TagDto tagDTO) {
        tagService.createTag(tagDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDto> getListOfTagDto() {
        return tagService.getListOfTagDto();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto getTagDtoById(@PathVariable("id") long id) {
        return tagService.getTagDtoById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTag(@PathVariable("id") long id) {
        tagService.deleteTag(id);
    }
}
