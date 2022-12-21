package com.offliner.rust.rust_offliner.exceptions;

public class IllegalMapParameterException extends RuntimeException {

    private int statusCode;

    public IllegalMapParameterException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
