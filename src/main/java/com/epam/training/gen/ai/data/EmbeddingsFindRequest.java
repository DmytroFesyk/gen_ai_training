package com.epam.training.gen.ai.data;

import java.util.List;

public record EmbeddingsFindRequest(
        String input,
        double maxDistance
) {
}
