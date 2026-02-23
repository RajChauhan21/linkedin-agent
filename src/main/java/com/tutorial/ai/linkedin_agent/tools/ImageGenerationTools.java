package com.tutorial.ai.linkedin_agent.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.ai.linkedin_agent.dto.News;
import com.tutorial.ai.linkedin_agent.dto.SaveDataRequest;
import com.tutorial.ai.linkedin_agent.service.CloudFlareImageService;
import com.tutorial.ai.linkedin_agent.service.LinkedInImageUploadService;
import com.tutorial.ai.linkedin_agent.service.NewsDataService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ImageGenerationTools {

    @Autowired
    private CloudFlareImageService imageService;

    @Autowired
    private NewsDataService newsDataService;

    @Autowired
    private LinkedInImageUploadService linkedInImageUploadService;

    @Tool(name = "image-generator",description = """
            Generate a high-quality image based on a detailed prompt.
                    INPUT: A string containing the image description prompt.
            """)
    public String generateImage(@ToolParam(description = "A string type which provides detailed image description") String prompt) {
        try {
            System.out.println(prompt);
            System.out.println("tool called");
            return imageService.getImage(prompt);
        } catch (IOException e) {
            return "failed to generate image";
        }
    }

    @Tool(name = "news-retriever", description = """
            Fetch the latest technology and IT-related news articles.
            Returns a list of news objects containing title, description, and category.
            """)
    public List<News> fetchLatestTechNews(@ToolParam(description = "Topic to search for, e.g., 'technology', 'IT industry', 'AI', 'cloud computing'") String topic) {
        System.out.println("tool news used");
        return newsDataService.getResponse(topic);
    }

    @Tool(name = "save-data", description = """
    ⚡ MANDATORY FINAL STEP - Saves image and news to database
    Call this ONLY when you have BOTH imageFileName from image-generator AND news from news-retriever
    """)
    public String saveInDatabase(@ToolParam(description = "Request data") Map<String, Object> requestData) {

        // Validate required fields
        if (!requestData.containsKey("imageFileName") || !requestData.containsKey("news")) {
            throw new IllegalArgumentException("Missing required fields: imageFileName and news are required");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();

            String imageFileName = (String) requestData.get("imageFileName");
            Map<String, Object> newsMap = (Map<String, Object>) requestData.get("news");

            // Validate news structure
            if (!newsMap.containsKey("title") || !newsMap.containsKey("description")) {
                throw new IllegalArgumentException("News object must contain title and description or text");
            }

            News news = mapper.convertValue(newsMap, News.class);

            System.out.println("✅ Successfully received data:");
            System.out.println("   - Image: " + imageFileName);
            System.out.println("   - News: " + news.getTitle());

            postOnLinkedIn(news);

            // Clear success message with summary
            return String.format(
                    "SUCCESS: Data saved! Title: '%s', Description: '%s', Image: %s",
                    news.getTitle(),
                    news.getDescription(),
                    imageFileName
            );

        } catch (Exception e) {
            System.err.println("❌ Error in save-data: " + e.getMessage());
            e.printStackTrace();
            // Always throw exceptions on failure
            throw new RuntimeException("Failed to execute save-data: " + e.getMessage(), e);
        }
    }

    public void postOnLinkedIn(News news){
        linkedInImageUploadService.postImageOnLinkedIn(news);
    }
}
