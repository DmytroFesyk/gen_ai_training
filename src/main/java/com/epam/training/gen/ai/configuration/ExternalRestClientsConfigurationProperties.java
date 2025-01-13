package com.epam.training.gen.ai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external-clients")
public record ExternalRestClientsConfigurationProperties(
        ExternalRestClientConfig weather
) {

    public record ExternalRestClientConfig(
            String url,
            String key
    ) {
    }
}
