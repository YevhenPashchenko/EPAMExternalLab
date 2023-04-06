package com.epam.esm.dbfiller.service;

import com.epam.esm.dbfiller.entity.giftcertificates.GiftCertificate;

import java.util.List;

/**
 * API provides methods for services {@link GiftCertificate} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateService {

    /**
     * Saves list of {@link GiftCertificate} objects.
     *
     * @param giftCertificates list of {@link GiftCertificate} objects.
     */
    void createGiftCertificates(List<GiftCertificate> giftCertificates);

    /**
     * Returns a {@link GiftCertificate} object by given {@link GiftCertificate} {@code id}.
     *
     * @param id {@code id}.
     * @return {@link GiftCertificate} object.
     */
    GiftCertificate getGiftCertificateById(Long id);
}
