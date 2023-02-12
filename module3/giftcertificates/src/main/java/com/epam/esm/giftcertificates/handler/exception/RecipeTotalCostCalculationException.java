package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class RecipeTotalCostCalculationException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 6862452135541075455L;

  public RecipeTotalCostCalculationException() {
    super("Cannot calculate recipe total cost");
  }
}
