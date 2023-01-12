package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;

/**
 * Interface that provides methods that must be implemented for CRUD operations with {@link GiftCertificateToTag}
 * in repository layer.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateToTagDao {

    /**
     * Method that create new {@link GiftCertificateToTag} entity in database.
     * @param giftCertificateToTag {@link GiftCertificateToTag} entity which data will be passed in database.
     */
    void create(GiftCertificateToTag giftCertificateToTag);
}
