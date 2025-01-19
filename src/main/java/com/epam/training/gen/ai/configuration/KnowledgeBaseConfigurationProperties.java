package com.epam.training.gen.ai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.knowledge-base")
public record KnowledgeBaseConfigurationProperties(
        double threshold,
        int limitTopK
) {
}
