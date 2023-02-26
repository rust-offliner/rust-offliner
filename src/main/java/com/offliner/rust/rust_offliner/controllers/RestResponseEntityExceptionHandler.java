package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.exceptions.ExceptionDto;
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
        return handleExceptionInternal(
                e,
                new ExceptionDto(HttpStatus.NOT_ACCEPTABLE.value(), "You can't follow a server you are already following"),
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                request);
    }
}
