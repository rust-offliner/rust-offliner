package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.datamodel.ServerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BattlemetricsServerService {

//    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private final WebClient webClient;

    @Autowired
    public BattlemetricsServerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ServerDTO getServer(int id) {
        return webClient
                .get()
                .uri("/servers/" + id + "?include=player&fields[player]=name,id,updatedAt&fields[server]=name,players,maxPlayers")
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
                .bodyToMono(ServerDTO.class)
                .block();
    }

}
