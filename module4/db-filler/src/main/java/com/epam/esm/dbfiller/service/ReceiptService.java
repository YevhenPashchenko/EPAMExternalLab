package com.epam.esm.dbfiller.service;

import com.epam.esm.dbfiller.entity.giftcertificates.Receipt;

import java.util.List;

/**
 * API provides methods for services {@link Receipt} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface ReceiptService {

    /**
     * Saves list of {@link Receipt} objects.
     *
     * @param receipts list of {@link Receipt} objects.
     */
    void createReceipt(List<Receipt> receipts);
}
