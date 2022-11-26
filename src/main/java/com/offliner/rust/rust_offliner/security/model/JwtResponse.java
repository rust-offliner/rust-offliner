package com.offliner.rust.rust_offliner.security.model;

import java.io.Serializable;

public record JwtResponse(String jwtToken) implements Serializable {

    private static final long serialVersionUID = 784357867684455L;

}
