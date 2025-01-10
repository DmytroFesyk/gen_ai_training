package com.epam.training.gen.ai.cleint;

import org.springframework.web.service.annotation.GetExchange;


public interface EpamDicalClient {

    @GetExchange("/openai/deployments")
    AvailableDialModelsResponse gatAvailableModels();
}
