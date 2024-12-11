package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.util.HashMap;

@RequiredArgsConstructor
public class ChatCompletionsConfigurationProvider {

    private final HashMap<String, Kernel> configuration = new HashMap<>();
    private final OpenAIAsyncClient openAIAsyncClient;

    public Kernel getKernel(String modelId) {
        return configuration.computeIfAbsent(modelId, this::createConfiguration);
    }

    @SneakyThrows
    public ChatCompletionService getChatCompletion(String modelId) {
        return configuration.computeIfAbsent(modelId, this::createConfiguration)
                .getService(ChatCompletionService.class);
    }

    private Kernel createConfiguration(String modelId) {
        val chat = OpenAIChatCompletion.builder()
                .withModelId(modelId)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
        return com.microsoft.semantickernel.Kernel.builder()
                .withAIService(ChatCompletionService.class, chat)
                .build();
    }


}
