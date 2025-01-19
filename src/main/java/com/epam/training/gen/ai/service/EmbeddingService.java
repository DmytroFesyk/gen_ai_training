package com.epam.training.gen.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final VectorStore vectorStore;


    public void saveEmbedding(List<String> inputs) {
        try {
            vectorStore.add(inputs.stream().map(input -> new Document(input)).toList());
        }catch (Exception e){
            log.error("Error saving embeddings", e.getCause());
            throw e;
        }
    }

    public List<Document> findEmbedding(String input, double threshold, int limit) {
        try {
            return vectorStore.similaritySearch(
                    SearchRequest.query(input)
                            .withTopK(limit)
                            .withSimilarityThreshold(threshold)
            );
        }catch (Exception e){
            log.error("Error finding embeddings", e.getCause());
            throw e;
        }
    }
}
