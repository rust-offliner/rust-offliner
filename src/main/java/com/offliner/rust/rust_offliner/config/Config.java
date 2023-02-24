package com.offliner.rust.rust_offliner.config;

import com.offliner.rust.rust_offliner.state.TrackingState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Configuration
public class Config {

//    @Value("${bm.apiKey}")
//    private String bmApiKey;

    @Value("${external.api.key}")
    private String key;

    @Value("${external.url}")
    private String url;

    @Bean
    public WebClient externalWebClient() {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", key)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // lock for synchronizing state between classes
    @Bean
    @Qualifier("lock")
    public Object stateLock() { return new Object(); }

//    @Bean
//    public AtomicLong paginationIndex() { return new AtomicLong(); }

//    @Bean
//    public TrackingState state() { return TrackingState.getInstance(); }

}
