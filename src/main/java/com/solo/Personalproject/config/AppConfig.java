package com.solo.Personalproject.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    String apiKey = "8010643278455002";
    String secretKey = "bOJwuyzDDsiW2Vzmtyj49mzL1NtQb3p1pYcm1S4IF2pTrpfyGVfiOQx4HZxpW6hoTMbZL54z07ClxsPW";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
