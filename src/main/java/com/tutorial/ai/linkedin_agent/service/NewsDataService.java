package com.tutorial.ai.linkedin_agent.service;

import com.tutorial.ai.linkedin_agent.dto.News;
import com.tutorial.ai.linkedin_agent.dto.Results;
import com.tutorial.ai.linkedin_agent.dto.Root;
import com.tutorial.ai.linkedin_agent.external.NewsDataExternalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsDataService {

    @Autowired
    private NewsDataExternalApi externalApi;

    @Value("${api.key}")
    private String apiKey;

    public List<News> getResponse(String p){
        Root data =  externalApi.getResponse(p, apiKey);

        List<News> news = new ArrayList<>();

       for (Results r : data.getResults()){
           if (news.size()==4) break;
           News news1 = new News();
           news1.setCategory(r.getCategory());
           news1.setTitle(r.getTitle());
           news1.setDescription(r.getDescription());
           news.add(news1);
       }

       return news;
    }
}
