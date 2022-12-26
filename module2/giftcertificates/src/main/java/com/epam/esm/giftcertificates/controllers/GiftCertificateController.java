package com.epam.esm.giftcertificates.controllers;

import com.epam.esm.giftcertificates.entities.dto.GiftCertificateDTO;
import com.epam.esm.giftcertificates.services.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    private static final String JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping()
    public void create(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        giftCertificateService.create(giftCertificateDTO);
    }

    @GetMapping(produces = JSON_MEDIA_TYPE)
    public List<GiftCertificateDTO> get() {
        return giftCertificateService.get();
    }

    @GetMapping(value = "/{id}", produces = JSON_MEDIA_TYPE)
    public GiftCertificateDTO getById(@PathVariable("id") long id) {
        return giftCertificateService.getById(id);
    }

    @GetMapping(value = "/sort", produces = JSON_MEDIA_TYPE)
    public List<GiftCertificateDTO> getByParameters(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return giftCertificateService.getByParameters(giftCertificateDTO);
    }

    @PatchMapping()
    public void update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        giftCertificateService.update(giftCertificateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
    }
}
