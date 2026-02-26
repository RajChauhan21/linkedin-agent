package com.tutorial.ai.linkedin_agent.config;

//import com.google.genai.Client;
import com.google.genai.Client;
import com.tutorial.ai.linkedin_agent.external.CloudFlareImageGenerator;
import com.tutorial.ai.linkedin_agent.external.LinkedInImageRegister;
import com.tutorial.ai.linkedin_agent.external.NewsDataExternalApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.ai.chat.client.ChatClient.builder;

@Configuration
public class ExternalServicesConfig {

    @Value("classpath:/system-prompt-gem.txt")
    private Resource systemPrompt;

    @Value("${spring.ai.openai.base-url}")
    private String openAiBaseUrl;

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${cloud.flare.api}")
    private String cloudFlareApiKey;

    @Bean
    public WebClient cloudFlareImageClient(){
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(a->a.defaultCodecs().maxInMemorySize(10*1024*1024))
                .build();

        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer "+cloudFlareApiKey)
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
    OpenAiApi openAiApi(){
        return OpenAiApi.builder()
                .apiKey(openAiApiKey)
                .baseUrl(openAiBaseUrl)
                .build();
    }

    @Bean
    OpenAiChatModel openAiChatModel(){
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi())
                .build();
    }

    @Bean
    public ChatClient chatClient(){
        return ChatClient.builder(openAiChatModel())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(OpenAiChatOptions.builder()
//                        .model("meta-llama/llama-4-scout-17b-16e-instruct")
                                .model("openai/gpt-oss-120b")
//                        .model("nvidia/nemotron-3-nano-30b-a3b:free")
                        .temperature(0.0d)
                        .maxTokens(4500)
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


    @Bean
    public Client genAiClient(){
        return Client.builder()
                .apiKey(googleApiKey)
                .vertexAI(true)
                .build();
    }
}
