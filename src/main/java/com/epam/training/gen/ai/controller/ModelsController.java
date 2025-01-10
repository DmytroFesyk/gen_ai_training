package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.data.AvailableModelsResponse;
import com.epam.training.gen.ai.service.EpamDialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelsController {

    private final EpamDialService epamDialService;


    @GetMapping(value = "/available-models", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AvailableModelsResponse getAvailableModels() {
        return new AvailableModelsResponse(epamDialService.getModels());
    }
}
