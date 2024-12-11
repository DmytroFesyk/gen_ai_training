package com.epam.training.gen.ai.controller.validator;

import com.epam.training.gen.ai.data.ChatSimpleRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChatSimpleRequestValidator implements Validator {

    private final PromptParametersValidator promptParametersValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChatSimpleRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        val request = (ChatSimpleRequest) target;
        if (request.promptParameters() == null) return;
        errors.pushNestedPath("promptParameters");
        ValidationUtils.invokeValidator(promptParametersValidator, request.promptParameters(), errors);
        errors.popNestedPath();
    }
}