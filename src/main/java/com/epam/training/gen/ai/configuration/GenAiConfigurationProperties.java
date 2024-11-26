package com.epam.training.gen.ai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.gen-ai")
public record GenAiConfigurationProperties(
        OpenAiClientProperties openaiClient,
        double temperature,
        int historyLimit
) {

    record OpenAiClientProperties(
            String endpoint,
            String key,
            String deploymentName
    ) {
    }
}
