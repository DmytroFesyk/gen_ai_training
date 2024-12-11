package com.epam.training.gen.ai.configuration;

import com.epam.training.gen.ai.cleint.EpamDicalClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final GenAiConfigurationProperties genAiConfigurationProperties;

    @Bean
    public RestClient dialRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(genAiConfigurationProperties.openaiClient().endpoint())
                .defaultHeader("Api-Key", genAiConfigurationProperties.openaiClient().key())
                .build();
    }


    @Bean
    public EpamDicalClient httpServiceProxyFactory(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(restClient)
        ).build().createClient(EpamDicalClient.class);
    }

}
