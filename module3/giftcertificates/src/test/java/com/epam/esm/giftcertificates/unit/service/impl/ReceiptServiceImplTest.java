package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.ReceiptDtoAssembler;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.ReceiptService;
import com.epam.esm.giftcertificates.service.PersonService;
import com.epam.esm.giftcertificates.service.impl.ReceiptServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

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

    private final GiftCertificateService giftCertificateService = mock(GiftCertificateService.class);
    private final PersonService personService = mock(PersonService.class);
    private final ReceiptDtoAssembler receiptDtoAssembler = mock(ReceiptDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Receipt> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final ReceiptRepository receiptRepository = mock(ReceiptRepository.class);
    private final ReceiptService receiptService = new ReceiptServiceImpl(giftCertificateService, personService,
        receiptDtoAssembler, pagedResourcesAssembler, receiptRepository);

    @Test
    void createReceipt_shouldReturnReceipt_whenExecutedNormally() {
        // GIVEN
        var receipt = TestEntityFactory.createDefaultReceiptDto();
        receipt.setGiftCertificates(List.of(TestEntityFactory.createDefaultGiftCertificateDto()));

        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();

        given(giftCertificateService.getGiftCertificateById(anyLong())).willReturn(giftCertificate);
        given(receiptDtoAssembler.toModel(any(Receipt.class))).willReturn(receipt);

        // WHEN
        var result = receiptService.createReceipt(0L, receipt);

        // THEN
        assertThat(EntityModel.of(receipt)).isEqualTo(result);
    }

    @Test
    void createReceipt_shouldThrowReceiptTotalCostCalculationException_whenGiftCertificatePriceIncorrect() {
        // THEN
        assertThrows(ReceiptTotalCostCalculationException.class,
            () -> receiptService.createReceipt(0L, new ReceiptDto()));
    }

    @Test
    void getAllOrders_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        receiptService.getAllReceipts(0L, 0, 2);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(receiptRepository.getAllByPersonId(0L, PageRequest.of(0, 2)), receiptDtoAssembler);
    }

    @Test
    void getReceiptById_shouldReturnReceipt_whenReceiptWithThisIdExist() {
        // GIVEN
        var receiptDto = TestEntityFactory.createDefaultReceiptDto();
        var receipt = TestEntityFactory.createDefaultReceipt();
        given(receiptRepository.findById(anyLong())).willReturn(Optional.of(receipt));
        given(receiptDtoAssembler.toModel(any(Receipt.class))).willReturn(receiptDto);

        // WHEN
        var result = receiptService.getReceiptById(0L);

        // THEN
        assertThat(EntityModel.of(receiptDto)).isEqualTo(result);
    }

    @Test
    void getReceiptById_shouldThrowEntityNotFoundException_whenReceiptWithThisIdNotExist() {
        // GIVEN
        given(receiptRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> receiptService.getReceiptById(0L));
    }
}
