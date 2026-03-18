package com.college.archive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenizationServiceTest {
    
    @Autowired
    private TokenizationService tokenizationService;
    
    @Test
    void testTokenize() {
        String text = "Data Structures and Algorithms for Computer Science";
        List<String> tokens = tokenizationService.tokenize(text);
        
        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertTrue(tokens.contains("data"));
        assertTrue(tokens.contains("structures"));
        assertTrue(tokens.contains("algorithms"));
    }
    
    @Test
    void testTokenizeEmptyString() {
        String text = "";
        List<String> tokens = tokenizationService.tokenize(text);
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }
    
    @Test
    void testExtractKeywords() {
        String text = "Java programming Java code Java development programming";
        List<String> keywords = tokenizationService.extractKeywords(text, 3);
        
        assertNotNull(keywords);
        assertTrue(keywords.size() <= 3);
        assertTrue(keywords.contains("java"));
    }
    
    @Test
    void testCalculateRelevance() {
        String query = "data structures";
        String document = "Introduction to Data Structures and Algorithms";
        
        double relevance = tokenizationService.calculateRelevance(query, document);
        
        assertTrue(relevance > 0);
        assertTrue(relevance <= 1.0);
    }
}
