package com.online_question_paper.online_question_paper_download_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // number of threads always running
        executor.setCorePoolSize(5);

        // maximum threads allowed
        executor.setMaxPoolSize(10);

        // waiting email queue size
        executor.setQueueCapacity(50);

        executor.setThreadNamePrefix("EmailThread-");

        executor.initialize();

        return executor;
    }
}