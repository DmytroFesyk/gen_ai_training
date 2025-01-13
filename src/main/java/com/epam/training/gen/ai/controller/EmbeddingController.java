package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.data.EmbeddingsFindRequest;
import com.epam.training.gen.ai.data.EmbeddingsFindResponse;
import com.epam.training.gen.ai.data.EmbeddingsRequest;
import com.epam.training.gen.ai.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void saveEmbeddings(@RequestBody EmbeddingsRequest request) {
        embeddingService.saveEmbedding(request.inputs());
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Stream<EmbeddingsFindResponse> findEmbeddings(@RequestBody EmbeddingsFindRequest request) {
        return embeddingService.findEmbedding(request.input(), 1.0 - request.maxDistance())
                .stream()
                .map(document -> new EmbeddingsFindResponse(
                        Optional.ofNullable(document.getMetadata().get("distance"))
                                .map(distance ->  (float) distance)
                                .map(distance -> (double) distance)
                                .orElse(0.0),
                        document.getContent()));
    }

//    @PostMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ChatWithHistoryResponse newChatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
//        var chatId = UUID.randomUUID();
//        return new ChatWithHistoryResponse(
//                chatId,
//                chatCompletionsService.completeWithHistory(request.input(), chatId, request.promptParameters())
//        );
//    }
//
//    @PutMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ChatWithHistoryResponse chatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
//        return new ChatWithHistoryResponse(
//                request.chatId(),
//                chatCompletionsService.completeWithHistory(request.input(), request.chatId(), request.promptParameters())
//        );
//    }
}
