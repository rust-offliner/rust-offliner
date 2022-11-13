package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.services.service_datamodel.ServerTemplate.ServerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ServerService {

//    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private final WebClient webClient;

    @Autowired
    public ServerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ServerTemplate getServer(int id) {
        return webClient
                .get()
                .uri("/servers/" + id + "?include=player&fields[player]=name,id,updatedAt&fields[server]=name,players,maxPlayers")
                .retrieve()
                .bodyToMono(ServerTemplate.class)
                .block();
    }

}
