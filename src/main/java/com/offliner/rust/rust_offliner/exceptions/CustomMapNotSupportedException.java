package com.offliner.rust.rust_offliner.exceptions;

public class CustomMapNotSupportedException extends Exception {
    public CustomMapNotSupportedException(String message) {
        super("Map is custom and we couldn't get it");
    }
}
