package com.offliner.rust.rust_offliner.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerNotTrackedException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(KeyAlreadyExistsException.class);
    public ServerNotTrackedException(String message) {
        log.error(message);
    }
}
