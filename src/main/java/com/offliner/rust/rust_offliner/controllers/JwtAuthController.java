package com.offliner.rust.rust_offliner.controllers;


import com.offliner.rust.rust_offliner.datamodel.UserDTO;
import com.offliner.rust.rust_offliner.security.JwtTokenUtil;
import com.offliner.rust.rust_offliner.services.security.JwtUserDetailsService;
import com.offliner.rust.rust_offliner.security.model.JwtRequest;
import com.offliner.rust.rust_offliner.security.model.JwtResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@CrossOrigin
public class JwtAuthController {

    private final Bucket bucket;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    public JwtAuthController() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    //    public JwtAuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.userDetailsService = userDetailsService;
//    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authentication) throws Exception {

        if (bucket.tryConsume(1)) {
            authenticate(authentication.getUsername(), authentication.getPassword());

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getUsername());

            final String token = jwtTokenUtil.generateUserToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
