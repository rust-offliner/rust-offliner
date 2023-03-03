package com.offliner.rust.rust_offliner.exceptions;

public class PrecedentEntityNotExistsException extends Exception {

    private final Types type;

    public PrecedentEntityNotExistsException(Types type) {
        this.type = type;
    }

    public enum Types {
        SERVER,
        MAP
    }

    public Types getType() {
        return type;
    }
}
