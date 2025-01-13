package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.cleint.weather.WeatherClient;
import com.epam.training.gen.ai.plugin.WeatherPlugin;
import com.epam.training.gen.ai.service.ChatCompletionsConfigurationProvider;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Configuration
@EnableConfigurationProperties({GenAiConfigurationProperties.class, ExternalRestClientsConfigurationProperties.class})
@RequiredArgsConstructor
public class Config {

    private final GenAiConfigurationProperties genAiConfigurationProperties;
    private final ExternalRestClientsConfigurationProperties externalRestClientsConfigurationProperties;

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
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
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
    public KernelPlugin wtherKernelPlugin(WeatherClient weatherClient) {
        return KernelPluginFactory.createFromObject(
                new WeatherPlugin(weatherClient, externalRestClientsConfigurationProperties.weather().key()), "weather");
    }

    @Bean
    public Kernel defaultkernel(ChatCompletionService chatCompletionService, List<KernelPlugin> kernelPlugins) {
        val builder = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService);

        kernelPlugins.forEach(builder::withPlugin);
        return builder.build();
    }

    @Bean
    public ChatCompletionsConfigurationProvider chatCompletionsConfigurationProvider(OpenAIAsyncClient openAIAsyncClient, List<KernelPlugin> kernelPlugins) {
        return new ChatCompletionsConfigurationProvider(openAIAsyncClient, kernelPlugins);
    }

    @Bean
    @RequestScope
    public ChatHistory pureChatHistory() {
        return new ChatHistory();
    }
}
