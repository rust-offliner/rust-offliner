package com.offliner.rust.rust_offliner.exceptions.controllers;

import com.offliner.rust.rust_offliner.exceptions.*;
import com.offliner.rust.rust_offliner.services.BucketAssignmentService;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    BucketAssignmentService service;

    @ExceptionHandler(value = { KeyAlreadyExistsException.class })
    public ResponseEntity<Object> handleServerAlreadyExistsException(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "You can't follow a server you are already following");
    }

    @ExceptionHandler(value = { MapStringIsNotValidBase64Exception.class })
    public ResponseEntity<Object> handleMapIsNotValidBase64Exception(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "Map image you provided is invalid");
    }

    @ExceptionHandler(value = { ImageExtensionNotSupportedException.class })
    public ResponseEntity<Object> handleImageExtensionNotSupportedException(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "Image you provided has an unsupported extension");
    }

    @ExceptionHandler(value = { ResolutionTooSmallException.class })
    public ResponseEntity<Object> handleTooSmallResolution(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "Image you provided has too small resolution");
    }

    @ExceptionHandler(value = { UnprocessableMapImageException.class })
    public ResponseEntity<Object> mapIsUnprocessable(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "There is something wrong with your image");
    }

    @ExceptionHandler(value = { ImageNotSquareException.class })
    public ResponseEntity<Object> mapIsNotSquare(
            Exception e, WebRequest request
    ) {
        return getObjectResponseEntity(e, request, "You poorly cropped the image - image must be square (or almost square)");
    }

    @NotNull
    private ResponseEntity<Object> getObjectResponseEntity(Exception e, WebRequest request, String message) {
        String username = getUsername();
        Bucket bucket = service.resolveBucket(username);
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
        return handleExceptionInternal(
                e,
                new ExceptionDto(status.value(), message),
                headers,
                status,
                request);
    }

    private String getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(String.valueOf(user == null));
        return user.getUsername();
    }
}
