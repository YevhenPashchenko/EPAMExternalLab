package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.constant.Authorities;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagDtoAssembler tagDtoAssembler;

    @PreAuthorize("hasRole(\"" + Authorities.ADMIN_ROLE + "\") && hasAuthority(\"" + Authorities.TAGS_WRITE + "\")")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TagDto> create(@Valid @RequestBody TagDto tag) {
        return tagService.createTag(tag);
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.TAGS_READ + "\")")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<TagDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return tagService.getAllTags(page, size);
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.TAGS_READ + "\")")
    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TagDto> getByName(@PathVariable("name") String name) {
        return EntityModel.of(tagDtoAssembler.toModel(tagService.getTagByName(name)));
    }

    @PreAuthorize("hasAnyRole(\"" + Authorities.ADMIN_ROLE + "\", \"" + Authorities.USER_ROLE + "\") && hasAuthority(\""
        + Authorities.TAGS_READ + "\")")
    @GetMapping(value = "/widely-used", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostReceipt(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return tagService.getMostWidelyUsedTagsFromPersonMaxCostReceipt(page, size);
    }

    @PreAuthorize("hasRole(\"" + Authorities.ADMIN_ROLE + "\") && hasAuthority(\"" + Authorities.TAGS_WRITE + "\")")
    @DeleteMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<TagDto> delete(@PathVariable("name") String name) {
        return tagService.deleteTag(name);
    }
}
