package com.tutorial.ai.linkedin_agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveDataRequest {

    @JsonProperty(namespace = "imageFileName",required = true)
    @JsonPropertyDescription("The exact filename or ID returned by the image-generator tool")
    private String imageFileName;

    @JsonProperty(namespace = "news", required = true)
    @JsonPropertyDescription("The title, description and list of category of the news article being saved")
    private News news;

}
