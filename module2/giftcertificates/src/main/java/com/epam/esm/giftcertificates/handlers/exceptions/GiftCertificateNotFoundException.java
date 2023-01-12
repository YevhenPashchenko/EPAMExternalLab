package com.epam.esm.giftcertificates.handlers.exceptions;

import java.io.Serial;

public class GiftCertificateNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4352949507598615804L;

    public GiftCertificateNotFoundException(long id) {
        super("Requested resource not found (id = " + id + ")");
    }

    public GiftCertificateNotFoundException(String message) {
        super(message);
    }
}
