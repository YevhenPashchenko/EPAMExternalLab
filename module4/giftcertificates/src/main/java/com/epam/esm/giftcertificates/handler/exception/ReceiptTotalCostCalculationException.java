package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class ReceiptTotalCostCalculationException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 8843537198563786646L;

    public ReceiptTotalCostCalculationException() {
        super("Cannot calculate recipe total cost");
    }
}
