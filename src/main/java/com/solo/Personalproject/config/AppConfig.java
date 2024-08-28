package com.solo.Personalproject.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    String apiKey = "8010643278455002";
    String secretKey = "JuOOk1x2YJX50wcHTJ5XEKgGFoC20GMie4kLYnVtUwYczhc4iRjKJCywRH6W7CA6ehEyrNSCImRUNOtR";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
