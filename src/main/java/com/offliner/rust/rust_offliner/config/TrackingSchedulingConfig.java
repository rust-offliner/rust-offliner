package com.offliner.rust.rust_offliner.config;

import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "schedular.tracking.enabled")
public class TrackingSchedulingConfig implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(TrackingSchedulingConfig.class);

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

//    @Qualifier("taskExecutor")
//    @Autowired
//    Executor taskExecutor;

    @Autowired
    TrackingServersService trackedServersService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                () -> {
                    try {
                        trackedServersService.track();
                    } catch (ServerNotTrackedException | KeyAlreadyExistsException e) {
                        log.error("giga sraka");
                    }
                },
                triggerContext -> {
                    Optional<Date> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletionTime());
                    Instant nextExecutionTime = lastCompletionTime.orElseGet(Date::new).toInstant()
                            .plusMillis(trackedServersService.getDelay());
                    return Date.from(nextExecutionTime);
                }
        );
    }
}
