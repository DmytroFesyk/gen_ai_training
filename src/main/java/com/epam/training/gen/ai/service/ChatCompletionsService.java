package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.data.ChatRoleMessage;
import com.epam.training.gen.ai.repository.ChatHistoryRepository;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatCompletionsService {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;
    private final ChatHistory chatHistory;
    private final ChatHistoryRepository chatHistoryRepository;
    @Value("${app.gen-ai.history-limit}")
    private int historyLimit;

    public String complete(String prompt) {
        chatHistory.addUserMessage(prompt);
        return Objects.requireNonNull(
                        chatCompletionService
                                .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                                .block())
                .stream()
                .map(ChatMessageContent::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public List<ChatRoleMessage> completeWithHistory(String prompt, UUID chatId) {
        var chatHistory = chatHistoryRepository.getChatHistory(chatId);
        chatHistory.addUserMessage(prompt);
        val chat = Objects.requireNonNull(
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                        .block());

        chatHistory.addAll(chat);

        val historyMessages = chatHistory.getMessages();

        if (historyMessages.size() > historyLimit){
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
}
