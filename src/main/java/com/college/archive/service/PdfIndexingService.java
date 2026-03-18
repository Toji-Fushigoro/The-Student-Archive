package com.college.archive.service;

import com.college.archive.entity.Document;
import com.college.archive.entity.PdfIndex;
import com.college.archive.repository.DocumentRepository;
import com.college.archive.repository.PdfIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfIndexingService {
    
    private final DocumentRepository documentRepository;
    private final PdfIndexRepository pdfIndexRepository;
    private final PdfTextExtractor pdfTextExtractor;
    private final TokenizationService tokenizationService;
    
    @Async
    @Transactional
    public void indexDocumentAsync(Long documentId) {
        try {
            indexDocument(documentId);
        } catch (Exception e) {
            log.error("Failed to index document {}", documentId, e);
        }
    }
    
    @Transactional
    public void indexDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));
        
        log.info("Starting indexing for document: {}", document.getTitle());
        
        String extractedText = pdfTextExtractor.extractText(document.getFileData());
        
        String tokenizedKeywords = tokenizationService.tokenizeToString(extractedText);
        
        PdfIndex existingIndex = pdfIndexRepository.findByDocument_Id(documentId).orElse(null);
        
        if (existingIndex != null) {
            existingIndex.setExtractedText(extractedText);
            existingIndex.setTokenizedKeywords(tokenizedKeywords);
            pdfIndexRepository.save(existingIndex);
            log.info("Updated index for document: {}", document.getTitle());
        } else {
            PdfIndex pdfIndex = PdfIndex.builder()
                .document(document)
                .extractedText(extractedText)
                .tokenizedKeywords(tokenizedKeywords)
                .build();
            pdfIndexRepository.save(pdfIndex);
            log.info("Created index for document: {}", document.getTitle());
        }
    }
    
    @Transactional
    public void deleteIndex(Long documentId) {
        pdfIndexRepository.deleteByDocument_Id(documentId);
        log.info("Deleted index for document: {}", documentId);
    }
}
