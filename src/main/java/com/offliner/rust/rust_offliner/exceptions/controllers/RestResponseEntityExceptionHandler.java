package com.offliner.rust.rust_offliner.exceptions.controllers;

import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.services.BucketAssignmentService;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
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
        String username = getUsername();
        Bucket bucket = service.resolveBucket(username);
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
        return handleExceptionInternal(
                e,
                new ExceptionDto(status.value(), "You can't follow a server you are already following"),
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
