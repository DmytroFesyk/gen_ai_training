package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.controller.validator.ChatSimpleRequestValidator;
import com.epam.training.gen.ai.controller.validator.ChatWithHistoryRequestValidator;
import com.epam.training.gen.ai.data.ChatSimpleRequest;
import com.epam.training.gen.ai.data.ChatSimpleResponse;
import com.epam.training.gen.ai.data.ChatWithHistoryRequest;
import com.epam.training.gen.ai.data.ChatWithHistoryResponse;
import com.epam.training.gen.ai.service.ChatCompletionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatCompletionsService chatCompletionsService;
    private final ChatWithHistoryRequestValidator chatWithHistoryRequest;
    private final ChatSimpleRequestValidator chatSimpleRequestValidator;

    @InitBinder("chatSimpleRequest")
    protected void initBinderSimpleValidator(WebDataBinder binder) {
        binder.addValidators(chatSimpleRequestValidator);
    }

    @InitBinder("chatWithHistoryRequest")
    protected void initBinderWithChatValidator(WebDataBinder binder) {
        binder.addValidators( chatWithHistoryRequest);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ChatSimpleResponse chat(@Valid @RequestBody ChatSimpleRequest request) {
        return new ChatSimpleResponse(chatCompletionsService.complete(request.input(), request.promptParameters()));
    }

    @PostMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ChatWithHistoryResponse newChatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
        var chatId = UUID.randomUUID();
        return new ChatWithHistoryResponse(
                chatId,
                chatCompletionsService.completeWithHistory(request.input(), chatId, request.promptParameters())
        );
    }

    @PutMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ChatWithHistoryResponse chatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
        return new ChatWithHistoryResponse(
                request.chatId(),
                chatCompletionsService.completeWithHistory(request.input(), request.chatId(), request.promptParameters())
        );
    }
}
