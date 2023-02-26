package com.offliner.rust.rust_offliner.exceptions.controllers;

import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { KeyAlreadyExistsException.class })
    public ResponseEntity<Object> handleServerAlreadyExistsException(
            Exception e, WebRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return handleExceptionInternal(
                e,
                new ExceptionDto(status.value(), "You can't follow a server you are already following"),
                new HttpHeaders(),
                status,
                request);
    }
}
