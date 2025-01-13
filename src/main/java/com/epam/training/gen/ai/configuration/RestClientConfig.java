package com.epam.training.gen.ai.configuration;

import com.epam.training.gen.ai.cleint.EpamDicalClient;
import com.epam.training.gen.ai.cleint.weather.WeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final GenAiConfigurationProperties genAiConfigurationProperties;
    private final ExternalRestClientsConfigurationProperties externalRestClientsConfigurationProperties;

    @Bean
    public RestClient dialRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(genAiConfigurationProperties.openaiClient().endpoint())
                .defaultHeader("Api-Key", genAiConfigurationProperties.openaiClient().key())
                .build();
    }


    @Bean
    public EpamDicalClient dialHttpServiceProxyFactory(RestClient dialRestClient) {
        return HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(dialRestClient)
        ).build().createClient(EpamDicalClient.class);
    }

    @Bean
    public RestClient weatherRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(externalRestClientsConfigurationProperties.weather().url())
                .build();
    }


    @Bean
    public WeatherClient weatherHttpServiceProxyFactory(RestClient weatherRestClient) {
        return HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(weatherRestClient)
        ).build().createClient(WeatherClient.class);
    }

}
