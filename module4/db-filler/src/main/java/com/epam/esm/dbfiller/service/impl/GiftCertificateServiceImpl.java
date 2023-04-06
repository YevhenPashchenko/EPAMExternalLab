package com.epam.esm.dbfiller.service.impl;

import com.epam.esm.dbfiller.repository.giftcertificates.GiftCertificateRepository;
import com.epam.esm.dbfiller.entity.giftcertificates.GiftCertificate;
import com.epam.esm.dbfiller.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createGiftCertificates(List<GiftCertificate> giftCertificates) {
        giftCertificateRepository.saveAll(giftCertificates);
    }

    @Override
    public GiftCertificate getGiftCertificateById(Long id) {
        return giftCertificateRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
