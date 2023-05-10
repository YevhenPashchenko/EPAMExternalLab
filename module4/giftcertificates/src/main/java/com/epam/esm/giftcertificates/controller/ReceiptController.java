package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.constant.Authorities;
import com.epam.esm.giftcertificates.dto.GiftCertificateNamesDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.service.ReceiptService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PreAuthorize("hasAnyRole(\"" + Authorities.ADMIN_ROLE + "\", \"" + Authorities.USER_ROLE + "\") && hasAuthority(\""
        + Authorities.RECEIPTS_WRITE + "\")")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ReceiptDto> create(@Valid @RequestBody GiftCertificateNamesDto giftCertificateNames) {
        return receiptService.createReceipt(giftCertificateNames);
    }

    @PreAuthorize("hasAnyRole(\"" + Authorities.ADMIN_ROLE + "\", \"" + Authorities.USER_ROLE + "\") && hasAuthority(\""
        + Authorities.RECEIPTS_READ + "\")")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<ReceiptDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return receiptService.getAllReceipts(page, size);
    }

    @PreAuthorize("hasAnyRole(\"" + Authorities.ADMIN_ROLE + "\", \"" + Authorities.USER_ROLE + "\") && hasAuthority(\""
        + Authorities.RECEIPTS_READ + "\")")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ReceiptDto> getById(@PathVariable("id") Long id) {
        return receiptService.getReceiptById(id);
    }
}
