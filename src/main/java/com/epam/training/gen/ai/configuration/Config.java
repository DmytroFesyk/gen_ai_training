package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.service.ChatCompletionsConfigurationProvider;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@EnableConfigurationProperties(GenAiConfigurationProperties.class)
@RequiredArgsConstructor
public class Config {

    private final GenAiConfigurationProperties genAiConfigurationProperties;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(genAiConfigurationProperties.openaiClient().key()))
                .endpoint(genAiConfigurationProperties.openaiClient().endpoint())
                .buildAsyncClient();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(genAiConfigurationProperties.temperature())
                        .build())
                .build();
    }

    @Bean
    public ChatCompletionService defaultChatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(genAiConfigurationProperties.openaiClient().deploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean
    public Kernel defaultkernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    @Bean
    public ChatCompletionsConfigurationProvider chatCompletionsConfigurationProvider(OpenAIAsyncClient openAIAsyncClient) {
        return new ChatCompletionsConfigurationProvider(openAIAsyncClient);
    }

    @Bean
    @RequestScope
    public ChatHistory pureChatHistory() {
        return new ChatHistory();
    }
}
