package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.ReceiptDtoAssembler;
import com.epam.esm.giftcertificates.dto.GiftCertificateNamesDto;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.ReceiptService;
import com.epam.esm.giftcertificates.service.impl.ReceiptServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class ReceiptServiceImplTest {

    private static final String EMAIL = "email@mail.com";
    private static final int PAGE = 0;
    private static final int SIZE = 2;
    private final GiftCertificateService giftCertificateService = mock(GiftCertificateService.class);
    private final ReceiptRepository receiptRepository = mock(ReceiptRepository.class);
    private final ReceiptDtoAssembler receiptDtoAssembler = mock(ReceiptDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Receipt> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final ReceiptService receiptService =
        new ReceiptServiceImpl(giftCertificateService, receiptRepository, receiptDtoAssembler, pagedResourcesAssembler);

    @Test
    void createReceipt_shouldReturnReceipt_whenExecutedNormally() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        var giftCertificateNames =
            TestEntityFactory.createDefaultGiftCertificateNamesDto(List.of(giftCertificate.getName()));
        var receipt =
            TestEntityFactory.createDefaultReceiptDto(List.of(TestEntityFactory.createDefaultGiftCertificateDto()));
        var authentication = new UsernamePasswordAuthenticationToken((Principal) () -> EMAIL, "password");
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        given(giftCertificateService.getGiftCertificateByName(giftCertificate.getName())).willReturn(giftCertificate);
        given(receiptDtoAssembler.toModel(any(Receipt.class))).willReturn(receipt);

        // WHEN
        var result = receiptService.createReceipt(giftCertificateNames);

        // THEN
        assertThat(EntityModel.of(receipt)).isEqualTo(result);
    }

    @Test
    void createReceipt_shouldThrowReceiptTotalCostCalculationException_whenGiftCertificatePriceIncorrect() {
        // THEN
        assertThrows(ReceiptTotalCostCalculationException.class,
            () -> receiptService.createReceipt(new GiftCertificateNamesDto()));
    }

    @Test
    void getAllReceipts_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        receiptService.getAllReceipts(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(receiptRepository.findAllByEmail(EMAIL, PageRequest.of(PAGE, SIZE)), receiptDtoAssembler);
    }

    @Test
    void getReceiptById_shouldReturnReceipt_whenReceiptWithThisIdExist() {
        // GIVEN
        var receipt =
            TestEntityFactory.createDefaultReceiptDto(List.of(TestEntityFactory.createDefaultGiftCertificateDto()));
        given(receiptRepository.findById(anyLong())).willReturn(Optional.of(
            TestEntityFactory.createDefaultReceipt(List.of(TestEntityFactory.createDefaultGiftCertificate()), EMAIL)));
        given(receiptDtoAssembler.toModel(any(Receipt.class))).willReturn(receipt);

        // WHEN
        var result = receiptService.getReceiptById(0L);

        // THEN
        assertThat(EntityModel.of(receipt)).isEqualTo(result);
    }

    @Test
    void getReceiptById_shouldThrowEntityNotFoundException_whenReceiptWithThisIdNotExist() {
        // GIVEN
        given(receiptRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> receiptService.getReceiptById(0L));
    }
}
