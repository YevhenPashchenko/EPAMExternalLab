package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entity.GiftCertificateToTag;

/**
 * The {@link GiftCertificateToTagDao} interface provides method for create {@link GiftCertificateToTag} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateToTagDao {

    /**
     * Saves {@link GiftCertificateToTag} object.
     * @param giftCertificateToTag given {@link GiftCertificateToTag} object which data will be saved.
     */
    void createGiftCertificateToTagLink(GiftCertificateToTag giftCertificateToTag);
}
