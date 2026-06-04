package com.example.lgauthservice.shared.infrastructure.thread;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@UtilityClass
public class Threads {

    public void sleep(Long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Bean(name = "commonAsyncExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setDaemon(true);
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("common-async-");
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
