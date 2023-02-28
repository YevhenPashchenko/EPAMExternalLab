package com.epam.esm.dbfiller.service.impl;

import com.epam.esm.dbfiller.repository.ReceiptRepository;
import com.epam.esm.dbfiller.entity.GiftCertificate;
import com.epam.esm.dbfiller.entity.Receipt;
import com.epam.esm.dbfiller.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Override
    @Transactional
    public void createReceipt(List<Receipt> receipts) {
        receiptRepository.saveAll(receipts);
    }
}
