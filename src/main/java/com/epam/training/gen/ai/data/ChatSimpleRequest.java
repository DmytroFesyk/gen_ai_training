package com.epam.training.gen.ai.data;

public record ChatSimpleRequest(
        PromptParameters promptParameters,
        String input
) {
}
