package com.epam.esm.giftcertificates.handlers.exceptions;

public class GiftCertificateNotFoundException extends RuntimeException {

    public GiftCertificateNotFoundException(long id) {
        super("Requested resource not found (id = " + id + ")");
    }
}
