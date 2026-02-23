package com.tutorial.ai.linkedin_agent.config;

import com.tutorial.ai.linkedin_agent.external.CloudFlareImageGenerator;
import com.tutorial.ai.linkedin_agent.external.LinkedInImageRegister;
import com.tutorial.ai.linkedin_agent.external.NewsDataExternalApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ExternalServicesConfig {

    @Value("classpath:/system-prompt-gem.txt")
    private Resource systemPrompt;

    @Bean
    public WebClient cloudFlareImageClient(){
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(a->a.defaultCodecs().maxInMemorySize(10*1024*1024))
                .build();

        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer 9321834217")
                .defaultHeader("Content-type","application/json")
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Bean
    public CloudFlareImageGenerator imageService(){
        HttpServiceProxyFactory build = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(cloudFlareImageClient())).build();
        return build.createClient(CloudFlareImageGenerator.class);
    }

    @Bean
    public WebClient newsDataWebClient(){
        return WebClient.builder()
                .build();
    }

    @Bean
    public NewsDataExternalApi newsDataExternalApi(){
        HttpServiceProxyFactory build = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(newsDataWebClient())).build();
        return build.createClient(NewsDataExternalApi.class);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder.defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.0)
                        .build())
                .defaultSystem(systemPrompt)
                .build();
    }

    @Bean
    public WebClient linkedInWebClient(){
        return WebClient.builder().baseUrl("https://api.linkedin.com/")
                .build();
    }

    @Bean
    public LinkedInImageRegister linkedInImageRegister(){
        HttpServiceProxyFactory serviceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(linkedInWebClient())).build();
        return serviceProxyFactory.createClient(LinkedInImageRegister.class);
    }
}
