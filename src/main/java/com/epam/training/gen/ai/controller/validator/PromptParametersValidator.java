package com.epam.training.gen.ai.controller.validator;

import com.epam.training.gen.ai.data.PromptParameters;
import com.epam.training.gen.ai.service.EpamDialService;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component
public class PromptParametersValidator implements Validator {

    private final EpamDialService epamDialService;
    private final List<String> forbiddenModelIds;

    public PromptParametersValidator(EpamDialService epamDialService,  @Value("${app.gen-ai.non-text-models}") List<String> forbiddenModelIds) {
        this.epamDialService = epamDialService;
        this.forbiddenModelIds = forbiddenModelIds;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PromptParameters.class.equals(clazz);
    }

    @Override
    public void validate(Object target, org.springframework.validation.Errors errors) {
        val parameters = (PromptParameters) target;
        Optional.ofNullable(parameters.temperature()).ifPresent(temperature -> {
            if (temperature < 0.0 || temperature > 1.0) {
                errors.rejectValue("temperature", "", "Temperature must be in range 0.0 - 1.0");
            }
        });

        Optional.ofNullable(parameters.modelId()).ifPresent(modelId -> {
            if (modelId.isBlank() || !epamDialService.getModels().contains(modelId)) {
                errors.rejectValue("modelId", "", "ModelId must be not blank and one available models.Please call /available-models endpoint to get available models");
            }
            if (forbiddenModelIds.contains(modelId)) {
                errors.rejectValue("modelId", "", "Selected model is not allowed for text generation");
            }
        });
    }
}
