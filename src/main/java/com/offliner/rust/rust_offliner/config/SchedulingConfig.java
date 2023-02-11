package com.offliner.rust.rust_offliner.config;

import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
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
public class SchedulingConfig implements SchedulingConfigurer {

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
                () -> trackedServersService.track(),
                triggerContext -> {
                    Optional<Date> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletionTime());
                    Instant nextExecutionTime = lastCompletionTime.orElseGet(Date::new).toInstant()
                            .plusMillis(trackedServersService.getDelay());
                    return Date.from(nextExecutionTime);
                }
        );
    }
}
