package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.data.ChatWithHistoryRequest;
import com.epam.training.gen.ai.data.ChatWithHistoryResponse;
import com.epam.training.gen.ai.service.EmbeddingService;
import com.epam.training.gen.ai.service.KnowledgeBaseCompletionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.pdfbox.Loader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RestController
@RequestMapping("/kb")
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final EmbeddingService embeddingService;

    private final KnowledgeBaseCompletionsService knowledgeBaseCompletionsService;

    @PostMapping(value = "/upload-pdf")
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public void uploadPdf(@RequestParam MultipartFile file) {
        if (!MediaType.APPLICATION_PDF_VALUE.equals(file.getContentType())) {
            throw new IllegalArgumentException("Content-Type '" + file.getContentType() + "' is not supported.");
        }
        val pdfFile = Loader.loadPDF(file.getBytes());
        embeddingService.saveEmbedding(pdfFile, file.getOriginalFilename());
    }

    @PostMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ChatWithHistoryResponse newChatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
        var chatId = UUID.randomUUID();
        return new ChatWithHistoryResponse(
                chatId,
                knowledgeBaseCompletionsService.completeWithHistory(request.input(), chatId, request.promptParameters())
        );
    }

    @PutMapping(value = "/with-history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ChatWithHistoryResponse chatWithHistory(@Valid @RequestBody ChatWithHistoryRequest request) {
        return new ChatWithHistoryResponse(
                request.chatId(),
                knowledgeBaseCompletionsService.completeWithHistory(request.input(), request.chatId(), request.promptParameters())
        );
    }
}
