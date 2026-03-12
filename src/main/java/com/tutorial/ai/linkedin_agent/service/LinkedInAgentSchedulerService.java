package com.tutorial.ai.linkedin_agent.service;

import com.tutorial.ai.linkedin_agent.tools.ImageGenerationTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class LinkedInAgentSchedulerService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ImageGenerationTools imageGenerationTools;


    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Kolkata") // 9 AM daily
    public void scheduledImageGeneration() {
        log.info("Starting scheduled image generation at: {}", System.currentTimeMillis());

        generateImageFromAI()
                .timeout(Duration.ofMinutes(5)) //wait for 5 minutes toget response from llm
                .retryWhen(Retry.backoff(3,Duration.ofSeconds(5)))
                .doOnNext(content -> log.info("Generated content: {}", content))
                .doOnComplete(() -> log.info("Image generation completed at: {}", System.currentTimeMillis()))
                .doOnError(error -> log.error("Error generating image: {}", error))
                .subscribe(); // Important: subscribe to consume the Flux
    }

    public Flux<String> generateImageFromAI() {
        return chatClient.prompt()
                .tools(imageGenerationTools)
                .user("Generate an image related to latest trend in AI industry")
                .stream()
                .content();
    }
}
