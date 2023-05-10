package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.GiftCertificateNamesDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Receipt;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link Receipt} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface ReceiptService {

    /**
     * Saves {@link Receipt} object.
     *
     * @param giftCertificateNames list of {@link GiftCertificate} object {@code name}.
     * @return {@link ReceiptDto} object.
     */
    EntityModel<ReceiptDto> createReceipt(GiftCertificateNamesDto giftCertificateNames);

    /**
     * Returns a list of {@link ReceiptDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link ReceiptDto} objects.
     */
    PagedModel<ReceiptDto> getAllReceipts(int page, int size);

    /**
     * Returns an {@link ReceiptDto} object by {@code id}.
     *
     * @param id object {@code id}.
     * @return {@link ReceiptDto} object.
     */
    EntityModel<ReceiptDto> getReceiptById(Long id);
}
