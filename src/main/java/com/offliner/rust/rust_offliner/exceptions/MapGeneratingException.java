package com.offliner.rust.rust_offliner.exceptions;

public class MapGeneratingException extends RuntimeException {

    private int statusCode;

    public MapGeneratingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public MapGeneratingException() {
    }

    public MapGeneratingException(Void unused) {

    }
}
