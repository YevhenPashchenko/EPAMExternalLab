package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.constant.Authorities;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagNamesDto;
import com.epam.esm.giftcertificates.dto.UpdateGiftCertificateDto;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gift-certificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateDtoAssembler giftCertificateDtoAssembler;

    @PreAuthorize(
        "hasRole(\"" + Authorities.ADMIN_ROLE + "\") && hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_WRITE + "\")")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> create(@Valid @RequestBody GiftCertificateDto giftCertificate) {
        return giftCertificateService.createGiftCertificate(giftCertificate);
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_READ + "\")")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return giftCertificateService.getAllGiftCertificates(page, size);
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_READ + "\")")
    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> getByName(@PathVariable("name") String name) {
        return EntityModel.of(
            giftCertificateDtoAssembler.toModel(giftCertificateService.getGiftCertificateByName(name)));
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_READ + "\")")
    @GetMapping(value = "/sort", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAllByParameters(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size,
        @RequestBody GiftCertificateSortingParametersDto giftCertificateSortingParameters) {
        return giftCertificateService.getAllGiftCertificatesByParameters(page, size,
            giftCertificateSortingParameters);
    }

    @PreAuthorize("hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_READ + "\")")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAllByTags(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size,
        @Valid @RequestBody TagNamesDto tagNames) {
        return giftCertificateService.getAllGiftCertificatesByTags(page, size, tagNames);
    }

    @PreAuthorize(
        "hasRole(\"" + Authorities.ADMIN_ROLE + "\") && hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_WRITE + "\")")
    @PatchMapping(value = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> update(@PathVariable("name") String name,
        @Valid @RequestBody UpdateGiftCertificateDto updateGiftCertificate) {
        return giftCertificateService.updateGiftCertificate(name, updateGiftCertificate);
    }

    @PreAuthorize(
        "hasRole(\"" + Authorities.ADMIN_ROLE + "\") && hasAuthority(\"" + Authorities.GIFT_CERTIFICATES_WRITE + "\")")
    @DeleteMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> delete(@PathVariable("name") String name) {
        return giftCertificateService.deleteGiftCertificate(name);
    }
}
