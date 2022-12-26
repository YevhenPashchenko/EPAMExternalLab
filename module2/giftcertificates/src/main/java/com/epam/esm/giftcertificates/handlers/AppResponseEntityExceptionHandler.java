package com.epam.esm.giftcertificates.handlers;

import com.epam.esm.giftcertificates.handlers.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.handlers.exceptions.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> tagHandleNotFound(Exception e) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setErrorCode(ErrorCodeUtil.TAG_NOT_FOUND);
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> giftCertificateNotFound(Exception e) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setErrorCode(ErrorCodeUtil.GIFT_CERTIFICATE_NOT_FOUND);
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> defaultExceptionHandler(Exception e) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
