package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.data.ChatSimpleRequest;
import com.epam.training.gen.ai.data.ChatSimpleResponse;
import com.epam.training.gen.ai.data.ChatWithHistoryRequest;
import com.epam.training.gen.ai.data.ChatWithHistoryResponse;
import com.epam.training.gen.ai.service.ChatCompletionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatCompletionsService chatCompletionsService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatSimpleResponse chat(@RequestBody ChatSimpleRequest request) {
        return new ChatSimpleResponse(chatCompletionsService.complete(request.input(),request.promptParameters()));
    }

    @PostMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatWithHistoryResponse newChatWithHistory(@RequestBody ChatWithHistoryRequest request) {
        var chatId = UUID.randomUUID();
        return new ChatWithHistoryResponse(
                chatId,
                chatCompletionsService.completeWithHistory(request.input(), chatId, request.promptParameters())
        );
    }

    @PutMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatWithHistoryResponse chatWithHistory(@RequestBody ChatWithHistoryRequest request) {
        return new ChatWithHistoryResponse(
                request.chatId(),
                chatCompletionsService.completeWithHistory(request.input(), request.chatId(), request.promptParameters())
        );
    }
}
