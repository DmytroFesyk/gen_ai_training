package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.cleint.AvailableDialModelsResponse;
import com.epam.training.gen.ai.cleint.EpamDicalClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpamDialService {

    private final EpamDicalClient epamDicalClient;

    public List<String> getModels() {
        return epamDicalClient.gatAvailableModels().models()
                .stream()
                .map(AvailableDialModelsResponse.AImodel::id)
                .toList();
    }
}
