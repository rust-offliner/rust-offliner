package com.offliner.rust.rust_offliner.security;

import com.offliner.rust.rust_offliner.services.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class TokenHandler {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    // this function returns new token with updated expiration date after every authenticated request
    public String handle(String authorization) {

        // remove "Bearer "
        String token = authorization.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return jwtTokenUtil.generateUserToken(userDetails);
    }
}
