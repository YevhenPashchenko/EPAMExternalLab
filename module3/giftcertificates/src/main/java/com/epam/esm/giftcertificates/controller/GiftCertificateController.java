package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> create(@Valid @RequestBody GiftCertificateDto giftCertificateDTO) {
        return giftCertificateService.createGiftCertificate(giftCertificateDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return giftCertificateService.getAllGiftCertificates(page, size);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> getById(@PathVariable("id") Long id) {
        return giftCertificateService.getGiftCertificateDtoById(id);
    }

    @GetMapping(value = "/sort", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAllByParameters(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size,
        @RequestBody GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
        return giftCertificateService.getAllGiftCertificatesByParameters(page, size,
            giftCertificateSortingParametersDto);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAllByTags(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size,
        @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.getAllGiftCertificatesByTags(page, size, giftCertificateDto);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<GiftCertificateDto> update(@PathVariable("id") Long id,
        @RequestBody GiftCertificateDtoForUpdate giftCertificateDtoForUpdate) {
        return giftCertificateService.updateGiftCertificate(id, giftCertificateDtoForUpdate);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        giftCertificateService.deleteGiftCertificate(id);
    }
}
