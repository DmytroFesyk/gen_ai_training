package com.epam.training.gen.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final VectorStore vectorStore;
    private final PdfHelperService pdfHelperService;

    public void saveEmbedding(PDDocument pdfFile, String fileName) {
        saveDocuments(pdfHelperService.extractTextFromPdf(pdfFile).stream().map(text -> new Document(text, Map.of("fileName", fileName))).toList());
    }


    public void saveEmbedding(List<String> inputs) {
        saveDocuments(inputs.stream().map(Document::new).toList());
    }

    private void saveDocuments(List<Document> documents) {
        executeWithRetries(() -> {
            vectorStore.add(documents);
            return true;
        });
    }

    public List<Document> findEmbedding(String input, double threshold, int limit) {

        return executeWithRetries(() -> vectorStore.similaritySearch(
                SearchRequest.query(input)
                        .withTopK(limit)
                        .withSimilarityThreshold(threshold)
        )).orElse(List.of());
    }

    private <T> Optional<T> executeWithRetries(Supplier<T> function) {
        var success = false;
        var attempt = 0;
        Optional<T> result = Optional.empty();
        while (attempt < 10 && !success) {
            try {
                result = Optional.of(function.get());
                success = true;
            } catch (Exception e) {
                log.error("Error finding embeddings", e.getCause());
            } finally {
                attempt++;
            }
        }
        return result;
    }
}
