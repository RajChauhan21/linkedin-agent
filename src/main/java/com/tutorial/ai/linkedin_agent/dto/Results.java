package com.tutorial.ai.linkedin_agent.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Results {

    public String article_id;
    public String title;
    public String description;
    public String content;
    public String language;
    public ArrayList<String> country;
    public ArrayList<String> category;
    public String datatype;
    public String fetched_at;
}
