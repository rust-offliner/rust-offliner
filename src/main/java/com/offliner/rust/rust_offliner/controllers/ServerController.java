package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.datamodel.RustMapsDTO;
import com.offliner.rust.rust_offliner.datamodel.TokenizedResponse;
import com.offliner.rust.rust_offliner.interfaces.IServerValidator;
import com.offliner.rust.rust_offliner.security.TokenHandler;
import com.offliner.rust.rust_offliner.services.BattlemetricsServerService;
import com.offliner.rust.rust_offliner.services.RustMapsService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    BattlemetricsServerService serverService;

    @Autowired
    RustMapsService rustMapsService;

    @Autowired
    TokenHandler tokenHandler;

    @Autowired
    IServerValidator serverDao;

    private final Bucket bucket;

    public ServerController() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<TokenizedResponse<?>> getServer(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            BattlemetricsServerDTO server = serverService.getServer(id);
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
    //    public Mono<ResponseEntity<TokenizedResponse<?>>> followServer(
        public Mono<?> followServer(
            @PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
//        SseEmitter emitter = new SseEmitter(0L);
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
//            try {
//                System.out.println(rustMapsService.getMap(74902653, 4500).flatMap(System.out::print));
//            } catch (Exception e) {
//                System.out.println("exception");
//            }
//            rustMapsService.getMap(id, 4250);
            return rustMapsService.getMap(id, 4250);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build());
    }
}

//    @PostMapping("query")
//    public Server returnServer(@RequestBody String query) {  }
