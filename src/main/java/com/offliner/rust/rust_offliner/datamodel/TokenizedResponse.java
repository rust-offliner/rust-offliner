package com.offliner.rust.rust_offliner.datamodel;

//public class TokenizedResponse<T> {
//
//    private String jwtToken;
//    private T t;
//
//    public TokenizedResponse(String jwtToken, T t) {
//        this.jwtToken = jwtToken;
//        this.t = t;
//    }
//}

import java.io.Serializable;

public record TokenizedResponse<T> (String jwtToken, long remainingRateLimit, T response) implements Serializable {

    private static final long serialVersionUID = 478245574545427447L;

}
