package com.offliner.rust.rust_offliner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class Config {

//    @Value("${bm.apiKey}")
//    private String bmApiKey;

    @Value("${battlemetrics.api.key}")
    private String key;

    @Bean
    public WebClient bmWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.battlemetrics.com")
                .defaultHeader("Authorization", key)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AtomicLong paginationIndex() { return new AtomicLong(); }

}
