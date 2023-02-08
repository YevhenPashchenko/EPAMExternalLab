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
import org.springframework.validation.annotation.Validated;
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
@Validated
public class GiftCertificateController {

  private final GiftCertificateService giftCertificateService;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<GiftCertificateDto> createGiftCertificate(
      @Valid @RequestBody GiftCertificateDto giftCertificateDTO) {
    return giftCertificateService.createGiftCertificate(giftCertificateDTO);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedModel<GiftCertificateDto> getAllGiftCertificates(
      @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0")
          int page,
      @RequestParam(defaultValue = "20")
          @Range(min = 1, max = 100, message = "size must be between 1 and 100")
          int size) {
    return giftCertificateService.getAllGiftCertificateDto(page, size);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<GiftCertificateDto> getGiftCertificateById(@PathVariable("id") long id) {
    return giftCertificateService.getGiftCertificateDtoById(id);
  }

  @GetMapping(
      value = "/sort",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedModel<GiftCertificateDto> getAllGiftCertificatesByParameters(
      @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0")
          int page,
      @RequestParam(defaultValue = "20")
          @Range(min = 1, max = 100, message = "size must be between 1 and 100")
          int size,
      @RequestBody GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
    return giftCertificateService.getAllGiftCertificateDtoByParameters(
        page, size, giftCertificateSortingParametersDto);
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<GiftCertificateDto> updateGiftCertificate(
      @RequestBody GiftCertificateDtoForUpdate giftCertificateDtoForUpdate) {
    return giftCertificateService.updateGiftCertificate(giftCertificateDtoForUpdate);
  }

  @DeleteMapping(value = "/{id}")
  public void deleteGiftCertificate(@PathVariable("id") long id) {
    giftCertificateService.deleteGiftCertificate(id);
  }
}
