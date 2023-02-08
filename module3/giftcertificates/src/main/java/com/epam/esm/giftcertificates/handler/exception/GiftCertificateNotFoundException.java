package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class GiftCertificateNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 4741656609525560101L;

  public GiftCertificateNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public GiftCertificateNotFoundException(String message) {
    super(message);
  }
}
