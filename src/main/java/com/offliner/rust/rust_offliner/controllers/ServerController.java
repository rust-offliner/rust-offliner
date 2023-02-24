package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.datamodel.TokenizedResponse;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.persistence.ServerDataStateManager;
import com.offliner.rust.rust_offliner.security.TokenHandler;
import com.offliner.rust.rust_offliner.services.EServerService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    EServerService serverService;

    public static final Logger log = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    ServerDataStateManager manager;
    @Autowired
    TokenHandler tokenHandler;

    private final Bucket bucket;

    public ServerController() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<TokenizedResponse<?>> getServer(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
//            try {
                //TODO change null and catch block
                EServerDto server = null;
                return ResponseEntity.ok(new TokenizedResponse(newToken, bucket.getAvailableTokens(), server));
//            } catch (ServerNotTrackedException e) {
//                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // server is not followed yet
//            }
            // TODO serwer jest nullem z jakiegos powodu
//            if (!server.isNull())
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

//    /* TODO
//        jesli serwer istnieje query do DB
//        INSERT INTO `server` VALUES(null, ipaddr, port, wipe_date)
//        query do rustmaps UNLESS custom map
//        INSERT INTO `maps` ...
//     */
    @PostMapping("/follow/{id}")
    public ResponseEntity<TokenizedResponse<?>> followServer(
            @PathVariable long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            try {
                manager.add(id);
                return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/{id}").buildAndExpand(id).toUri())
                        .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                        .build();
            } catch (KeyAlreadyExistsException e) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new TokenizedResponse<>(newToken, bucket.getAvailableTokens(), e.getMessage()));
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
