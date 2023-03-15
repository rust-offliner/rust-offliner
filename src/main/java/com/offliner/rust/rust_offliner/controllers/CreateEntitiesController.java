package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.bases.BaseManager;
import com.offliner.rust.rust_offliner.datamodel.BaseDto;
import com.offliner.rust.rust_offliner.exceptions.*;
import com.offliner.rust.rust_offliner.exceptions.bases.CoordsOutOfBoundsException;
import com.offliner.rust.rust_offliner.exceptions.maps.ImageExtensionNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.maps.ImageNotSquareException;
import com.offliner.rust.rust_offliner.exceptions.maps.UnprocessableMapImageException;
import com.offliner.rust.rust_offliner.maps.MapImage;
import com.offliner.rust.rust_offliner.maps.MapManager;
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
@RequestMapping("/api/create")
@Slf4j
public class CreateEntitiesController {

    EServerService serverService;

    ServerDataStateManager manager;

    TokenHandler tokenHandler;

    JwtTokenUtil jwtTokenUtil;

    BucketAssignmentService service;

    MapManager mapManager;

    private Bucket bucket;

    private BaseManager baseManager;

    public CreateEntitiesController(EServerService serverService,
                                    ServerDataStateManager manager,
                                    TokenHandler tokenHandler,
                                    JwtTokenUtil jwtTokenUtil,
                                    BucketAssignmentService service,
                                    MapManager mapManager, BaseManager baseManager) {
        this.serverService = serverService;
        this.manager = manager;
        this.tokenHandler = tokenHandler;
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
        this.mapManager = mapManager;
//        bucket = service.resolveBucket();
        this.baseManager = baseManager;
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
    public ResponseEntity<?> followServer(
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

    @PostMapping("/map/{id}")
    public ResponseEntity<?> addMap(
            @PathVariable long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody String imageB64
    ) throws ResolutionTooSmallException, ImageNotSquareException, MapStringIsNotValidBase64Exception, UnprocessableMapImageException, ImageExtensionNotSupportedException, PrecedentEntityNotExistsException {
        bucket = service.resolveBucket(getUsername());
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            log.info(imageB64);
            MapImage image = new MapImage(id, imageB64);
            mapManager.userSave(id, image);
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/{id}").buildAndExpand(id).toUri())
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .header("X-Api-Key", newToken)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }


    @PostMapping("/base")
    public ResponseEntity<?> addBase(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody BaseDto base
    ) throws CoordsOutOfBoundsException {
        bucket = service.resolveBucket(getUsername());
        if (bucket.tryConsume(1)) {
            String newToken = tokenHandler.handle(authorization);
            baseManager.save(base);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private String getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(String.valueOf(user == null));
        return user.getUsername();
    }

}
