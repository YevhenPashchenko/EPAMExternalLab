package com.epam.esm.giftcertificates.services;

import com.epam.esm.giftcertificates.entities.dto.GiftCertificateDTO;

import java.util.List;

public interface GiftCertificateService {

    void create(GiftCertificateDTO giftCertificateDTO);

    List<GiftCertificateDTO> get();

    GiftCertificateDTO getById(long id);

    List<GiftCertificateDTO> getByParameters(GiftCertificateDTO giftCertificateDTO);

    void update(GiftCertificateDTO giftCertificateDTO);

    void delete(long id);
}
