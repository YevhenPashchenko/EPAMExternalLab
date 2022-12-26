package com.epam.esm.giftcertificates.handlers;

import lombok.Data;

@Data
public class CustomErrorResponse {

    private int errorCode;
    private String errorMessage;
}
