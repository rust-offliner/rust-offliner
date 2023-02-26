package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.datamodel.TokenizedResponse;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.persistence.ServerDataStateManager;
import com.offliner.rust.rust_offliner.security.JwtTokenUtil;
import com.offliner.rust.rust_offliner.security.TokenHandler;
import com.offliner.rust.rust_offliner.services.BucketAssignmentService;
import com.offliner.rust.rust_offliner.services.EServerService;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/follow")
@Slf4j
public class CreateEntitiesController {

    EServerService serverService;

    ServerDataStateManager manager;

    TokenHandler tokenHandler;

    JwtTokenUtil jwtTokenUtil;

    BucketAssignmentService service;

    private Bucket bucket;

    public CreateEntitiesController(EServerService serverService, ServerDataStateManager manager, TokenHandler tokenHandler, JwtTokenUtil jwtTokenUtil, BucketAssignmentService service) {
        this.serverService = serverService;
        this.manager = manager;
        this.tokenHandler = tokenHandler;
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
//        bucket = service.resolveBucket();
    }

//    public CreateEntitiesController() {
////        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
////        this.bucket = Bucket.builder().addLimit(limit).build();
//        bucket = service.resolveBucket();
//    }

//    @PostMapping("/{id}")
//    public ResponseEntity<TokenizedResponse<?>> getServer(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
//        if (bucket.tryConsume(1)) {
//            String newToken = tokenHandler.handle(authorization);
////            try {
//                //TODO change null and catch block
//                EServerDto server = null;
//                return ResponseEntity.ok(new TokenizedResponse(newToken, bucket.getAvailableTokens(), server));
////            } catch (ServerNotTrackedException e) {
////                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // server is not followed yet
////            }
//            // TODO serwer jest nullem z jakiegos powodu
////            if (!server.isNull())
////                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
//    }

//    /* TODO
//        jesli serwer istnieje query do DB
//        INSERT INTO `server` VALUES(null, ipaddr, port, wipe_date)
//        query do rustmaps UNLESS custom map
//        INSERT INTO `maps` ...
//     */
    @PostMapping("/{id}")
    public ResponseEntity<TokenizedResponse<?>> followServer(
            @PathVariable long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws KeyAlreadyExistsException {
        bucket = service.resolveBucket(getUsername());
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            manager.add(id);
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/{id}").buildAndExpand(id).toUri())
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .header("X-Api-Key", newToken)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private String getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(String.valueOf(user == null));
        return user.getUsername();
    }

}
