package com.tutorial.ai.linkedin_agent.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Root {
    public String status;
    public int totalResults;
    public ArrayList<Results> results;
    public String nextPage;
}
