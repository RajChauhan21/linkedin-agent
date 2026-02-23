package com.tutorial.ai.linkedin_agent.external;

import com.tutorial.ai.linkedin_agent.dto.ImagePrompt;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "https://image-api.craj57477.workers.dev/", accept = MediaType.IMAGE_JPEG_VALUE)
public interface CloudFlareImageGenerator {

    @PostExchange
    public byte[] generateImage(@RequestBody ImagePrompt prompt);
}
