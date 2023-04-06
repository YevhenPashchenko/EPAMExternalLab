package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.ReceiptDtoAssembler;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.dto.GiftCertificateNamesDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import com.epam.esm.giftcertificates.repository.ReceiptRepository;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final GiftCertificateService giftCertificateService;
    private final ReceiptRepository receiptRepository;
    private final ReceiptDtoAssembler receiptDtoAssembler;
    private final PagedResourcesAssembler<Receipt> pagedResourcesAssembler;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    @Override
    @Transactional
    public EntityModel<ReceiptDto> createReceipt(GiftCertificateNamesDto giftCertificateNames) {
        var receipt = new Receipt();
        receipt.setGiftCertificates(giftCertificateNames.getGiftCertificateNames()
            .stream()
            .map(giftCertificateService::getGiftCertificateByName)
            .toList());
        receipt.setTotalCost(
            receipt.getGiftCertificates().stream().map(GiftCertificate::getPrice).reduce(BigDecimal::add).orElseThrow(
                ReceiptTotalCostCalculationException::new));
        receipt.setEmail(getEmail());
        receiptRepository.save(receipt);
        return EntityModel.of(receiptDtoAssembler.toModel(receipt));
    }

    private String getEmail() {
        return securityContextHolderStrategy.getContext().getAuthentication().getName();
    }

    @Override
    public PagedModel<ReceiptDto> getAllReceipts(int page, int size) {
        return pagedResourcesAssembler.toModel(receiptRepository.findAllByEmail(getEmail(), PageRequest.of(page, size)),
            receiptDtoAssembler);
    }

    @Override
    public EntityModel<ReceiptDto> getReceiptById(Long id) {
        return EntityModel.of(receiptDtoAssembler.toModel(receiptRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Requested receipt not found (id = " + id + ")",
                HttpErrorCodes.RECEIPT_NOT_FOUND))));
    }
}
