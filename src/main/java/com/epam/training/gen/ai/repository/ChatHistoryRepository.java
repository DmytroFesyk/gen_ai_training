package com.epam.training.gen.ai.repository;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class ChatHistoryRepository {

    private final HashMap<UUID, ChatHistory> chatHistoryMap = new HashMap<>();

    public ChatHistory getChatHistory(UUID chatId) {
        return chatHistoryMap.getOrDefault(chatId, new ChatHistory());
    }

    public void saveChatHistory(UUID chatId, ChatHistory chatHistory) {
        chatHistoryMap.put(chatId, chatHistory);
    }

}
