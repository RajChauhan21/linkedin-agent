package com.tutorial.ai.linkedin_agent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.ai.linkedin_agent.dto.ImagePrompt;
import com.tutorial.ai.linkedin_agent.external.CloudFlareImageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
public class CloudFlareImageService {

    @Autowired
    private CloudFlareImageGenerator imageService;

    @Autowired
    private LinkedInImageUploadService linkedInImageUploadService;

    @Value("${app.upload.dir}")
    private String uploadDirectory;

    public String getImage(String request) throws IOException {

        try {
            String refinedPrompt = cleanPrompt(request);
            ImagePrompt prompt = new ImagePrompt(refinedPrompt);
            byte[] bytes = imageService.generateImage(prompt);
            String fileName = "cloudf" + System.currentTimeMillis() + ".jpg";

            Path uploadDir = Path.of(uploadDirectory);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, bytes);
            if (bytes.length != 0) {
                System.out.println(" linkedin api accessed");
//                linkedInImageUploadService.registerImageOnLinkedIn(bytes);
            }
            return fileName;
        } catch (Exception e) {
            return e.toString();
        }
    }

    private String cleanPrompt(String prompt) {
        if (prompt == null) return "";

        // Remove BOM and other hidden characters
        String cleaned = prompt.replace("\uFEFF", "") // Remove BOM
                .replace("\0", "")      // Remove null characters
                .trim();

        // If it looks like JSON, extract the actual prompt
        if (cleaned.startsWith("{") && cleaned.endsWith("}")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(cleaned);
                if (node.has("prompt")) {
                    cleaned = node.get("prompt").asText();
                } else if (node.has("request")) {
                    cleaned = node.get("request").asText();
                } else if (node.has("arguments")) {
                    JsonNode args = node.get("arguments");
                    if (args.has("prompt")) {
                        cleaned = args.get("prompt").asText();
                    }
                }
            } catch (Exception e) {
                System.out.println("Not valid JSON, using as-is");
            }
        }

        // Remove any surrounding quotes
        cleaned = cleaned.replaceAll("^\"|\"$", "");

        // Remove escape characters
        cleaned = cleaned.replace("\\\"", "\"")
                .replace("\\\\", "\\");

        return cleaned.trim();
    }
}
