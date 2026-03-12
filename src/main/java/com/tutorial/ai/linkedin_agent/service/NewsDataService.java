package com.tutorial.ai.linkedin_agent.service;

import com.tutorial.ai.linkedin_agent.dto.News;
import com.tutorial.ai.linkedin_agent.dto.Results;
import com.tutorial.ai.linkedin_agent.dto.Root;
import com.tutorial.ai.linkedin_agent.external.NewsDataExternalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsDataService {

    @Autowired
    private NewsDataExternalApi externalApi;

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RetryTemplate retryTemplate;

    public List<News> getResponse(String p){
        Root data =  retryTemplate.execute(context -> externalApi.getResponse(p, apiKey), //wrap under retry template
                context -> new Root()); // added a fallback

        List<News> news = new ArrayList<>();

       if (data!=null){
           for (Results r : data.getResults()){
               if (news.size()==4) break;
               News news1 = new News();
               news1.setCategory(r.getCategory());
               news1.setTitle(r.getTitle());
               news1.setDescription(r.getDescription());
               news.add(news1);
           }
       }
       else {
           News news1 = new News();
           news1.setCategory(List.of("Artificial Intelligence", "Technology"));
           news1.setTitle("Artificial Intelligence");
           news1.setDescription("""
                   In the context of the 2026 news landscape, Artificial Intelligence (AI), 
                   Large Language Models (LLMs), and Agents represent a shift from purely 
                   automated "content generation" to intelligent, autonomous editorial workflows. 
                   While early 2020s AI was used for simple transcription, by 2026 these technologies 
                   act as "junior colleagues" that research, draft, edit, and distribute content,
                    moving from routine task automation to proactive, multi-step journalism
                   """);
           news.add(news1);
       }

       return news;
    }
}
