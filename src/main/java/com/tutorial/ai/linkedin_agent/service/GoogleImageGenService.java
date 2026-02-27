package com.tutorial.ai.linkedin_agent.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateImagesConfig;
import com.google.genai.types.GenerateImagesResponse;
import com.google.genai.types.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GoogleImageGenService {

    @Autowired
    private Client client;

    @Value("${app.upload.dir}")
    private String directory;

    @Autowired
    private LinkedInImageUploadService linkedInImageUploadService;

    public String generateImageUsingGoogleVertex(String prompt) throws IOException {
        GenerateImagesConfig config = GenerateImagesConfig.builder()
                .addWatermark(true)
                .numberOfImages(1)
                .outputMimeType("image/jpeg")
                .includeSafetyAttributes(true)
                .build();

        GenerateImagesResponse response = client.models.generateImages( "imagen-3.0-generate-002", prompt, config);

        if (response.images().isEmpty()) {
            System.out.println("Unable to generate images.");
        }
        System.out.println("Generated " + response.images().size() + " images.");
        Image generatedImage = response.images().get(0);

        Path uploadDir = Path.of(directory);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String filename = "image_" + System.currentTimeMillis() + ".jpeg";
        Path filePath = uploadDir.resolve(filename);

        Files.write(filePath, generatedImage.imageBytes().get());
        if (generatedImage.imageBytes().isPresent()){
            linkedInImageUploadService.registerImageOnLinkedIn( generatedImage.imageBytes().get());
        }
         return filename;
    }
}
