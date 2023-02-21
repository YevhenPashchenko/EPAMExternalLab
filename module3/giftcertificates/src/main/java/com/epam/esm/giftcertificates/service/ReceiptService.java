package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.entity.Person;
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
     * @param id         {@link Person} object {@code id}.
     * @param receiptDto {@link ReceiptDto} object.
     * @return {@link Receipt} object.
     */
    EntityModel<ReceiptDto> createReceipt(Long id, ReceiptDto receiptDto);

    /**
     * Returns a list of {@link Person} {@link ReceiptDto} objects from given {@code page} and {@code size}.
     *
     * @param id   {@link Person} object {@code id}.
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link ReceiptDto} objects.
     */
    PagedModel<ReceiptDto> getAllReceipts(Long id, int page, int size);

    /**
     * Returns an {@link ReceiptDto} object by {@code id}.
     *
     * @param id object {@code id}.
     * @return {@link ReceiptDto} object.
     */
    EntityModel<ReceiptDto> getReceiptById(Long id);
}
