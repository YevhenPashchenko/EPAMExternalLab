package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class OrderTotalCostCalculationException extends RuntimeException {

  @Serial private static final long serialVersionUID = 6862452135541075455L;

  public OrderTotalCostCalculationException() {
    super("Cannot calculate order total cost");
  }
}
