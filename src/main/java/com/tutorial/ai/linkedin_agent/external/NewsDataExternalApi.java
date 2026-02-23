package com.tutorial.ai.linkedin_agent.external;

import com.tutorial.ai.linkedin_agent.dto.Root;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://newsdata.io/api/1")
public interface NewsDataExternalApi {

    @GetExchange("/latest?country=in&language=en&category=technology,science&removeduplicate=1&sort=relevancy&excludefield=link,source_id,source_name,source_url,source_icon,source_priority,keywords,creator,image_url,video_url,pubdate,pubdatetz,ai_tag,sentiment,sentiment_stats,ai_region,ai_org,duplicate,ai_summary")
    Root getResponse(@RequestParam("q") String p, @RequestParam("apikey") String key);
}

