package com.college.archive.service;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenizationService {
    
    private final Tokenizer tokenizer;
    private final Set<String> stopWords;
    
    public TokenizationService() {
        this.tokenizer = SimpleTokenizer.INSTANCE;
        this.stopWords = initializeStopWords();
        log.info("TokenizationService initialized with SimpleTokenizer");
    }
    
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String[] tokens = tokenizer.tokenize(text.toLowerCase());
        
        return Arrays.stream(tokens)
            .filter(token -> !token.trim().isEmpty())
            .filter(token -> token.length() > 2)
            .filter(token -> !stopWords.contains(token))
            .map(this::normalizeToken)
            .distinct()
            .collect(Collectors.toList());
    }
    
    public String tokenizeToString(String text) {
        List<String> tokens = tokenize(text);
        return String.join(" ", tokens);
    }
    
    public List<String> extractKeywords(String text, int maxKeywords) {
        List<String> tokens = tokenize(text);
        
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String token : tokens) {
            frequencyMap.put(token, frequencyMap.getOrDefault(token, 0) + 1);
        }
        
        return frequencyMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(maxKeywords)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    public double calculateRelevance(String query, String documentText) {
        List<String> queryTokens = tokenize(query);
        List<String> docTokens = tokenize(documentText);
        
        if (queryTokens.isEmpty() || docTokens.isEmpty()) {
            return 0.0;
        }
        
        Set<String> docTokenSet = new HashSet<>(docTokens);
        long matchCount = queryTokens.stream()
            .filter(docTokenSet::contains)
            .count();
        
        return (double) matchCount / queryTokens.size();
    }
    
    private String normalizeToken(String token) {
        return token.replaceAll("[^a-z0-9]", "");
    }
    
    private Set<String> initializeStopWords() {
        return new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
            "has", "he", "in", "is", "it", "its", "of", "on", "that", "the",
            "to", "was", "will", "with", "the", "this", "but", "they", "have",
            "had", "what", "when", "where", "who", "which", "why", "how"
        ));
    }
}
