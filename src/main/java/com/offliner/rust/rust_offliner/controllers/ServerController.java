package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.datamodel.ServerDTO;
import com.offliner.rust.rust_offliner.datamodel.TokenizedResponse;
import com.offliner.rust.rust_offliner.security.TokenHandler;
import com.offliner.rust.rust_offliner.services.BattlemetricsServerService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    BattlemetricsServerService serverService;

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
            ServerDTO server = serverService.getServer(id);
            if (!server.isNull())
                return ResponseEntity.ok(new TokenizedResponse(newToken, bucket.getAvailableTokens(), server));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    /* TODO
        jesli serwer istnieje query do DB
        INSERT INTO `server` VALUES(null, ipaddr, port, wipe_date)
        query do rustmaps UNLESS custom map
        INSERT INTO `maps` ...
     */
    @PostMapping("/follow/{id}")
    public ResponseEntity<TokenizedResponse<?>> followServer(
            @PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            return ResponseEntity.ok(new TokenizedResponse(newToken, bucket.getAvailableTokens(), null));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

//    @PostMapping("query")
//    public Server returnServer(@RequestBody String query) {  }

}
