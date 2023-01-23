package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
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
    public void createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDTO) {
        giftCertificateService.createGiftCertificate(giftCertificateDTO);
    }

    @GetMapping(value = "/page/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDto> getListOfGiftCertificateDtoForPage(@PathVariable("number") int pageNumber) {
        return giftCertificateService.getListOfGiftCertificateDtoForPage(pageNumber);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto getGiftCertificateDtoById(@PathVariable("id") long id) {
        return giftCertificateService.getGiftCertificateDtoById(id);
    }

    @GetMapping(value = "/sort/page/{number}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDto> getListOfGiftCertificateDtoByParametersForPage(@PathVariable("number") int pageNumber, @RequestBody GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
        return giftCertificateService.getListOfGiftCertificateDtoByParametersForPage(pageNumber, giftCertificateSortingParametersDto);
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDTO) {
        giftCertificateService.updateGiftCertificate(giftCertificateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteGiftCertificate(@PathVariable("id") long id) {
        giftCertificateService.deleteGiftCertificate(id);
    }
}
