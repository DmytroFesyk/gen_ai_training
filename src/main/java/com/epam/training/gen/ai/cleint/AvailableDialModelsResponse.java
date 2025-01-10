package com.epam.training.gen.ai.cleint;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AvailableDialModelsResponse(
        @JsonProperty("data")
        List<AImodel> models
) {
    public record AImodel(
            String id,
            String status
    ) {
    }
}
