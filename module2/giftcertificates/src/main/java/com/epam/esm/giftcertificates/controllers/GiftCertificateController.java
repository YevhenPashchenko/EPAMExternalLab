package com.epam.esm.giftcertificates.controllers;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.services.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody GiftCertificateDto giftCertificateDTO) {
        giftCertificateService.create(giftCertificateDTO);
    }

    @GetMapping(value = "/page/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDto> get(@PathVariable("number") int pageNumber) {
        return giftCertificateService.get(pageNumber);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto getById(@PathVariable("id") long id) {
        return giftCertificateService.getById(id);
    }

    @GetMapping(value = "/sort/page/{number}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDto> getByParameters(@PathVariable("number") int pageNumber, @RequestBody GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
        return giftCertificateService.getByParameters(pageNumber, giftCertificateSortingParametersDto);
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody GiftCertificateDto giftCertificateDTO) {
        giftCertificateService.update(giftCertificateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
    }
}
