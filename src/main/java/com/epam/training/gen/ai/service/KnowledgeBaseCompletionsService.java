package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.configuration.KnowledgeBaseConfigurationProperties;
import com.epam.training.gen.ai.data.ChatRoleMessage;
import com.epam.training.gen.ai.data.PromptParameters;
import com.epam.training.gen.ai.repository.ChatHistoryRepository;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.val;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeBaseCompletionsService extends ChatCompletionsService {

    private final KnowledgeBaseConfigurationProperties properties;

    private final EmbeddingService embeddingService;

    private static final String SYSTEM_MESSAGE = """
              Assistant helps the company workers with various question related to predefined context.
              Be brief in your answers.
              If no related or not enough information found in the provided context- answer 'I don't know.I can assist only with searching information in our knowledge base.'.
              Do not generate answers that don't use the context.
              Do invent facts and paraphrase text from the context, be as close to the context as possible when giving an answer
              If asking a clarifying question to the user would help, ask the question.
              The context is provided in the message below with <<<< and >>>> markers.
              If the context value is - "The context was not provided" - answer 'I don't know.I can assist only with searching information in our knowledge base.'
              Answer the question strictly referring the provided context!
            """;

    private static final String USER_MESSAGE = """
              Answer the question strictly referring the provided context:
              <<<<
              %s
              >>>>
            """;

    public KnowledgeBaseCompletionsService(
            ChatCompletionService defaultChatCompletionService,
            Kernel defaultKernel,
            InvocationContext defaultInvocationContext,
            ChatHistory chatHistory,
            ChatHistoryRepository chatHistoryRepository,
            ChatCompletionsConfigurationProvider configProvider, KnowledgeBaseConfigurationProperties properties, EmbeddingService embeddingService
    ) {
        super(defaultChatCompletionService, defaultKernel, defaultInvocationContext, chatHistory, chatHistoryRepository, configProvider);
        this.properties = properties;
        this.embeddingService = embeddingService;
    }


    public List<ChatRoleMessage> completeWithHistory(String prompt, UUID chatId, PromptParameters promptParameters) {
        val context = embeddingService.findEmbedding(prompt, properties.threshold(), properties.limitTopK())
                .stream().map(Document::getContent).findFirst();
        val chatHistory = chatHistoryRepository.getChatHistory(chatId);
        if (chatHistory.getMessages().isEmpty()) {
            chatHistory.addSystemMessage(SYSTEM_MESSAGE);
        }
        val contextMessage = context
                .map(c -> String.format(USER_MESSAGE, c))
                .orElseGet(() -> String.format(USER_MESSAGE, "The context was not provided"));
        chatHistory.addUserMessage(contextMessage);
        chatHistoryRepository.saveChatHistory(chatId, chatHistory);
        return super.completeWithHistory(prompt, chatId, promptParameters);
    }

}
