package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.datamodel.RustMapsDTO;
import com.offliner.rust.rust_offliner.exceptions.IllegalMapParameterException;
import com.offliner.rust.rust_offliner.exceptions.MapGeneratingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class RustMapsService {

    private final WebClient webClient;

    @Autowired
    public RustMapsService(@Qualifier("RustMaps") WebClient webClient) {
        System.out.println("2");
        this.webClient = webClient;
    }

    /**
     *
     * @param seed - seed of map
     * @param size - size of map - Integer within range [1000;6000] with step 50
     * @return RustMapsDTO object
     *
     * this function gathers a Mono from a map generation source
     *
     * when the source throws 404 it means we need to call another endpoint to request
     * generation of our map
     *
     * on error 409 we need to wait for the map to be generated, it usually takes
     * around 3 minutes, which we don't want to block
     */
    public Mono<RustMapsDTO> getMap(int seed, int size) {

        final int finalSize;

        if (size > 6000)
            size = 6000;
        if (size < 1000)
            size = 1000;

        // floor to the nearest value divisible by 50
        if (size % 50 != 0)
            size -= size % 50;

        finalSize = size;

        return webClient.get()
                .uri(seed + "/" + finalSize)
                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//                    if (clientResponse.statusCode().equals(HttpStatus.CONFLICT)) {
//                        return Mono.error(Exception::new);
//                    } else {  // the only "else" case is HTTP404 Not Found
//                        return Mono.error(Exception::new);
//                    }
//                .onStatus(HttpStatus::is4xxClientError, res -> {
//                    res.toEntity(String.class).subscribe(
//                            entity -> System.out.println("Client error " + entity)
//                    );
//                    return Mono.error(new Exception(String.valueOf(res.statusCode())));})

                .onRawStatus(status -> status == HttpStatus.CONFLICT.value(), clientResponse -> {
//                    System.out.println("CONFLICT");
                    return Mono.error(new MapGeneratingException("Map is generating", clientResponse.rawStatusCode()));
                })
                .onRawStatus(status -> status == HttpStatus.NOT_FOUND.value(), clientResponse -> {
//                    System.out.println("404");
//                    requestMapGeneration(seed, finalSize);
//                    return Mono.error(new MapGeneratingException("Map is generating", clientResponse.rawStatusCode()))
//                            .flatMap(res -> requestMapGeneration(seed, finalSize));
//                    return Mono.just(requestMapGeneration(seed, finalSize))
//                            .flatMap(res -> Mono.error(new MapGeneratingException("Map is generating", clientResponse.rawStatusCode())));
                    return requestMapGeneration(seed, finalSize)
//                            .flatMap(res -> Mono.error(new MapGeneratingException("Map is generating", clientResponse.rawStatusCode())));
                            .thenReturn(new MapGeneratingException());
                })

//                .onStatus(HttpStatus::is2xxSuccessful, clientResponse -> Mono.error(new Exception("200")))
                .bodyToMono(RustMapsDTO.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(8))
                        .doAfterRetry(a -> System.out.println("XDDDWTF"))
                    .filter(throwable -> throwable instanceof MapGeneratingException));
//                .flatMap(response -> return Mono.just());
    }

    private Mono<Void> requestMapGeneration(int seed, int size) {
        System.out.println(":D");
        return webClient.post()
                .uri(seed + "/" + size)
                .retrieve()
                .onRawStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> {
                    return Mono.error(new IllegalMapParameterException("Wrong seed/size", clientResponse.rawStatusCode()));
                })
                .bodyToMono(Void.class);
    }
}

//    public Mono<SomeClass> get(int value) {
//        return webClient.get()
//                .uri("/" + value)
//                .retrieve()
//                .onRawStatus(HttpStatus.CONFLICT::equals, clientResponse -> {
//                    return Mono.error(new SomeException1("Some message", clientResponse.rawStatusCode()));
//                })
//                .onRawStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> {
//                    requestGeneration(value);
//                    return Mono.error(new SomeException1("Some message", clientResponse.rawStatusCode()));
//                })
//                .bodyToMono(SomeClass.class)
//                .retryWhen(Retry.backoff(5, Duration.ofSeconds(8))
//                        .filter(throwable -> throwable instanceof SomeException1));
//    }
//
//    private Mono<Void> requestGeneration(int value) {
//        return webClient.post()
//                .uri("/" + value)
//                .retrieve()
//                .onRawStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> {
//                    return Mono.error(new SomeException2("Wrong value", clientResponse.rawStatusCode()));
//                })
//                .bodyToMono(Void.class);
//    }
