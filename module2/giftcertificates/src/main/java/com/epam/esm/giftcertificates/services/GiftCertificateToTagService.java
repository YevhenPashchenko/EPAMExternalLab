package com.epam.esm.giftcertificates.services;

import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;

/**
 * Interface that provides methods that must be implemented for operations with {@link GiftCertificateToTag}
 * in service layer.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateToTagService {

    /**
     * Method that prepares data from {@link GiftCertificateToTagDto} to convert them in {@link GiftCertificateToTag}
     * and then passes to repository layer method which should create new {@link GiftCertificateToTag} in database.
     * @param giftCertificateToTagDTO {@link GiftCertificateToTagDto}.
     */
    void create(GiftCertificateToTagDto giftCertificateToTagDTO);
}
