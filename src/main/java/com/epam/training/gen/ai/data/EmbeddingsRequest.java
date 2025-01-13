package com.epam.training.gen.ai.data;

import java.util.List;

public record EmbeddingsRequest(
        List<String> inputs
) {
}
