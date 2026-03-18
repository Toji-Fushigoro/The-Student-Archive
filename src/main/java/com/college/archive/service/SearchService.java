package com.college.archive.service;

import com.college.archive.dto.DocumentSearchResult;
import com.college.archive.entity.Document;
import com.college.archive.entity.PdfIndex;
import com.college.archive.entity.SearchHistory;
import com.college.archive.entity.User;
import com.college.archive.repository.DocumentRepository;
import com.college.archive.repository.PdfIndexRepository;
import com.college.archive.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    
    private final DocumentRepository documentRepository;
    private final PdfIndexRepository pdfIndexRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final TokenizationService tokenizationService;
    
    @Transactional
    public List<DocumentSearchResult> search(String query, User user, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        log.info("Searching for: {} by user {}", query, user.getEmail());
        
        List<String> queryTokens = tokenizationService.tokenize(query);
        log.debug("Query tokens: {}", queryTokens);
        
        List<PdfIndex> allIndexes = pdfIndexRepository.findAll();
        
        Map<Long, Double> relevanceScores = new HashMap<>();
        
        for (PdfIndex index : allIndexes) {
            double score = calculateRelevanceScore(queryTokens, index);
            if (score > 0) {
                relevanceScores.put(index.getDocument().getId(), score);
            }
        }
        
        List<DocumentSearchResult> results = relevanceScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .map(entry -> {
                Document doc = documentRepository.findById(entry.getKey()).orElse(null);
                return doc != null ? DocumentSearchResult.fromDocument(doc, entry.getValue()) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        saveSearchHistory(query, results.size(), user);
        
        log.info("Found {} results for query: {}", results.size(), query);
        return results;
    }
    
    @Transactional
    public Page<Document> searchByKeyword(String keyword, Pageable pageable) {
        return documentRepository.searchByKeyword(keyword, pageable);
    }
    
    @Transactional
    public Page<Document> searchByCategory(Document.DocumentCategory category, Pageable pageable) {
        return documentRepository.findByCategory(category, pageable);
    }
    
    @Transactional
    public Page<Document> searchBySubject(String subject, Pageable pageable) {
        return documentRepository.findBySubjectContainingIgnoreCase(subject, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<SearchHistory> getUserSearchHistory(User user) {
        return searchHistoryRepository.findTop10ByUser_IdOrderBySearchedAtDesc(user.getId());
    }
    
    private double calculateRelevanceScore(List<String> queryTokens, PdfIndex index) {
        if (queryTokens.isEmpty() || index.getTokenizedKeywords() == null) {
            return 0.0;
        }
        
        String indexedContent = (index.getTokenizedKeywords() + " " + 
                               index.getDocument().getTitle() + " " +
                               index.getDocument().getSubject()).toLowerCase();
        
        List<String> contentTokens = Arrays.asList(indexedContent.split("\\s+"));
        Set<String> contentTokenSet = new HashSet<>(contentTokens);
        
        long matchCount = queryTokens.stream()
            .filter(contentTokenSet::contains)
            .count();
        
        double baseScore = (double) matchCount / queryTokens.size();
        
        boolean titleMatch = index.getDocument().getTitle().toLowerCase()
            .contains(String.join(" ", queryTokens));
        if (titleMatch) {
            baseScore *= 1.5;
        }
        
        return baseScore;
    }
    
    private void saveSearchHistory(String query, int resultsCount, User user) {
        SearchHistory history = SearchHistory.builder()
            .user(user)
            .searchQuery(query)
            .resultsCount(resultsCount)
            .build();
        searchHistoryRepository.save(history);
    }
}
