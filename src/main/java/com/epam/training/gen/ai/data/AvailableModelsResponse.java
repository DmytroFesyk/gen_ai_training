package com.epam.training.gen.ai.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AvailableModelsResponse(
        List<String> models
) {
}
