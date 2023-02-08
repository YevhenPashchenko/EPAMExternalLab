package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class OrderNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -7372869782636206505L;

  public OrderNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public OrderNotFoundException(String message) {
    super(message);
  }
}
