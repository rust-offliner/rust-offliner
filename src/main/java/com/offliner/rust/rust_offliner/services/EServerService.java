package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EServerService {

//    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private final WebClient webClient;

    @Autowired
    public EServerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public EServerDto getServer(long id) {
        return webClient
                .get()
                .uri("/servers/" + id + "?include=player&fields[player]=name,id,updatedAt&fields[server]=name,ip,port,players,maxPlayers,details")
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
                .bodyToMono(EServerDto.class)
                .block();
    }

    public String getServerString(long id) {
        return webClient
                .get()
                .uri("/servers/" + id + "?include=player&fields[player]=name,id,updatedAt&fields[server]=name,ip,port,players,maxPlayers,details")
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
                .bodyToMono(String.class)
                .block();
    }


}
