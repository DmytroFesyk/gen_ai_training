package com.epam.training.gen.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final VectorStore vectorStore;


    public void saveEmbedding(List<String> inputs) {
        vectorStore.add(inputs.stream().map(input -> new Document(input)).toList());
    }

    public List<Document> findEmbedding(String input, double threshold) {
        return vectorStore.similaritySearch(
                SearchRequest.query(input)
                        .withTopK(5)
                        .withSimilarityThreshold(threshold)
        );
    }
}
