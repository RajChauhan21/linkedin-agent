package com.tutorial.ai.linkedin_agent.controller;

import com.tutorial.ai.linkedin_agent.service.CloudFlareImageService;
import com.tutorial.ai.linkedin_agent.service.LinkedInImageUploadService;
import com.tutorial.ai.linkedin_agent.service.NewsDataService;
import com.tutorial.ai.linkedin_agent.tools.ImageGenerationTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AgentController {

    @Autowired
    private NewsDataService externalService;

    @Autowired
    private CloudFlareImageService imageService;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ImageGenerationTools imageGenerationTools;

    @Autowired
    private LinkedInImageUploadService linkedInImageUploadService;


    @PostMapping("/call")
    public Flux<String> generateImageFromAI(@RequestBody String prompt) {
        return chatClient.prompt()
                .tools(imageGenerationTools)
                .user(prompt)
                .stream()
                .content();
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestNews(@RequestParam("q") String p) {
        return new ResponseEntity<>(externalService.getResponse(p), HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerImageUploadOnLinkedIn(@RequestBody byte[] bytes) {
        return new ResponseEntity<>(linkedInImageUploadService.registerImageOnLinkedIn(bytes),HttpStatus.ACCEPTED);
    }

    @PostMapping("/img")
    public ResponseEntity<?> getImage(@RequestBody String request) throws IOException {
        return new ResponseEntity<>(imageService.getImage(request), HttpStatus.ACCEPTED);
    }
}
