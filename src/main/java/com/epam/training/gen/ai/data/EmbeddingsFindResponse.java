package com.epam.training.gen.ai.data;

public record EmbeddingsFindResponse(
        double distanceToInput,
        String payload
) {
}
