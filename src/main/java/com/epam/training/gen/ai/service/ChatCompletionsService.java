package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.data.ChatRoleMessage;
import com.epam.training.gen.ai.data.PromptParameters;
import com.epam.training.gen.ai.repository.ChatHistoryRepository;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatCompletionsService {

    private final ChatCompletionService defaultChatCompletionService;
    private final Kernel defaultKernel;
    private final InvocationContext defaultInvocationContext;
    private final ChatHistory chatHistory;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatCompletionsConfigurationProvider configProvider;
    @Value("${app.gen-ai.history-limit}")
    private int historyLimit;

    public String complete(String prompt, PromptParameters promptParameters) {
        chatHistory.addUserMessage(prompt);
        val invocationContext = InvocationContext.copy(defaultInvocationContext).withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY).build();
        return executeCompletion(chatHistory, promptParameters, invocationContext)
                .stream()
                .map(ChatMessageContent::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public List<ChatRoleMessage> completeWithHistory(String prompt, UUID chatId, PromptParameters promptParameters) {
        var chatHistory = chatHistoryRepository.getChatHistory(chatId);
        chatHistory.addUserMessage(prompt);
        val chat = executeCompletion(chatHistory, promptParameters).stream()
                .filter(chatMessageContent -> chatMessageContent.getAuthorRole() != AuthorRole.ASSISTANT || chatMessageContent.getContent() != null)
                .toList();

        chatHistory.addAll(chat);

        val historyMessages = chatHistory.getMessages();

        if (historyMessages.size() > historyLimit) {
            chatHistory = new ChatHistory(
                    historyMessages.stream()
                            .skip(Math.max(historyMessages.size() - historyLimit, 0))
                            .toList()
            );
        }

        chatHistoryRepository.saveChatHistory(chatId, chatHistory);
        return chatHistory.getMessages()
                .stream()
                .map(chatMessageContent -> new ChatRoleMessage(chatMessageContent.getAuthorRole(), chatMessageContent.getContent()))
                .collect(Collectors.toList());
    }

    private List<ChatMessageContent<?>> executeCompletion(ChatHistory chatHistory, PromptParameters promptParameters) {
        val chatCompletionService = getChatCompletionService(promptParameters);
        return Objects.requireNonNull(
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, getKernel(promptParameters), getInvocationContext(promptParameters))
                        .block());
    }

    private List<ChatMessageContent<?>> executeCompletion(ChatHistory chatHistory, PromptParameters promptParameters, InvocationContext initialInvocationContext) {
        val chatCompletionService = getChatCompletionService(promptParameters);
        return Objects.requireNonNull(
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, getKernel(promptParameters), getInvocationContext(initialInvocationContext, promptParameters))
                        .block());
    }

    private ChatCompletionService getChatCompletionService(PromptParameters promptParameters) {
        return Optional.ofNullable(promptParameters)
                .map(PromptParameters::modelId)
                .map(configProvider::getChatCompletion)
                .orElse(defaultChatCompletionService);
    }

    private Kernel getKernel(PromptParameters promptParameters) {
        return Optional.ofNullable(promptParameters)
                .map(PromptParameters::modelId)
                .map(configProvider::getKernel)
                .orElse(defaultKernel);
    }

    private InvocationContext getInvocationContext(PromptParameters promptParameters) {
        return getInvocationContext(defaultInvocationContext, promptParameters);
    }

    private InvocationContext getInvocationContext(InvocationContext initialInvocationContext, PromptParameters promptParameters) {
        return Optional.ofNullable(promptParameters)
                .map(PromptParameters::temperature)
                .map(temperature -> InvocationContext.copy(initialInvocationContext)
                        .withPromptExecutionSettings(
                                PromptExecutionSettings.builder()
                                        .withTemperature(temperature)
                                        .build()
                        )
                        .build()
                )
                .orElse(initialInvocationContext);
    }
}
