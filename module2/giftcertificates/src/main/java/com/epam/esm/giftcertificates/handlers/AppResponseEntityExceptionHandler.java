package com.epam.esm.giftcertificates.handlers;

import com.epam.esm.giftcertificates.dto.ErrorDto;
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
    public ResponseEntity<ErrorDto> tagHandleNotFound(Exception exception) {
        var error = ErrorDto.builder()
                .code(ErrorCodeUtil.TAG_NOT_FOUND)
                .message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<ErrorDto> giftCertificateNotFound(Exception exception) {
        return new ResponseEntity<>(ErrorDto.builder()
                .code(ErrorCodeUtil.GIFT_CERTIFICATE_NOT_FOUND)
                .message(exception.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(Exception exception) {
        return new ResponseEntity<>(ErrorDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
