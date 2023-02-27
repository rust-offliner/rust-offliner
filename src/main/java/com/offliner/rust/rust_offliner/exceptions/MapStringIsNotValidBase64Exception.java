package com.offliner.rust.rust_offliner.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapStringIsNotValidBase64Exception extends Exception {
    public MapStringIsNotValidBase64Exception(String message) { log.error(message); }
}
