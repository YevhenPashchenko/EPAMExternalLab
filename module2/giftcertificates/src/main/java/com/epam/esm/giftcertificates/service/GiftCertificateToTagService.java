package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificateToTag;

/**
 * The {@link GiftCertificateToTagService} interface provides method for service {@link GiftCertificateToTag} objects data
 * before create it.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateToTagService {

    /**
     * Saves {@link GiftCertificateToTagDto} object.
     * @param giftCertificateToTagDTO {@link GiftCertificateToTagDto} object.
     */
    void createGiftCertificateToTagLink(GiftCertificateToTagDto giftCertificateToTagDTO);
}
