package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.ReceiptDtoAssembler;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.ReceiptService;
import com.epam.esm.giftcertificates.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final GiftCertificateService giftCertificateService;
    private final PersonService personService;
    private final ReceiptDtoAssembler receiptDtoAssembler;
    private final PagedResourcesAssembler<Receipt> pagedResourcesAssembler;
    private final ReceiptRepository receiptRepository;

    @Override
    @Transactional
    public EntityModel<ReceiptDto> createReceipt(Long id, ReceiptDto receiptDto) {
        var receipt = new Receipt();
        var giftCertificates = receiptDto.getGiftCertificates().stream().map(GiftCertificateDto::getId)
            .map(giftCertificateService::getGiftCertificateById).toList();
        receipt.setGiftCertificates(giftCertificates);
        receipt.setTotalCost(giftCertificates.stream().map(GiftCertificate::getPrice).reduce(BigDecimal::add)
            .orElseThrow(ReceiptTotalCostCalculationException::new));
        receipt.setPerson(personService.getPersonById(id));
        receiptRepository.save(receipt);
        return EntityModel.of(receiptDtoAssembler.toModel(receipt));
    }

    @Override
    public PagedModel<ReceiptDto> getAllReceipts(Long id, int page, int size) {
        return pagedResourcesAssembler.toModel(receiptRepository.getAllByPersonId(id, PageRequest.of(page, size)),
            receiptDtoAssembler);
    }

    @Override
    public EntityModel<ReceiptDto> getReceiptById(Long id) {
        return EntityModel.of(receiptDtoAssembler.toModel(receiptRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Requested resource not found (id = " + id + ")",
                HttpErrorCodes.RECEIPT_NOT_FOUND))));
    }
}
