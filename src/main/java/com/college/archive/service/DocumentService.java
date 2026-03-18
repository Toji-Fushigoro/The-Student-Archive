package com.college.archive.service;

import com.college.archive.dto.DocumentUploadRequest;
import com.college.archive.entity.Document;
import com.college.archive.entity.User;
import com.college.archive.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final PdfTextExtractor pdfTextExtractor;
    private final PdfIndexingService pdfIndexingService;
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String PDF_MIME_TYPE = "application/pdf";
    
    @Transactional
    public Document uploadDocument(MultipartFile file, 
                                  DocumentUploadRequest request, 
                                  User uploader) throws IOException {
        validateFile(file);
        
        byte[] fileData = file.getBytes();
        
        Document document = Document.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .category(request.getCategory())
            .subject(request.getSubject())
            .year(request.getYear())
            .semester(request.getSemester())
            .uploadedBy(uploader)
            .fileData(fileData)
            .fileSize((long) fileData.length)
            .filename(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .build();
        
        Document savedDocument = documentRepository.save(document);
        log.info("Document uploaded: {} by user {}", savedDocument.getTitle(), uploader.getEmail());
        
        pdfIndexingService.indexDocumentAsync(savedDocument.getId());
        
        return savedDocument;
    }
    
    @Transactional(readOnly = true)
    public Document getDocument(Long id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found: " + id));
    }
    
    @Transactional
    public void deleteDocument(Long id, User user) {
        Document document = getDocument(id);
        
        if (!user.getRole().equals(User.UserRole.ADMIN) && 
            !document.getUploadedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this document");
        }
        
        documentRepository.delete(document);
        log.info("Document deleted: {} by user {}", document.getTitle(), user.getEmail());
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds maximum limit of 10MB");
        }
        
        String contentType = file.getContentType();
        if (!PDF_MIME_TYPE.equals(contentType)) {
            throw new RuntimeException("Only PDF files are allowed");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("File must have .pdf extension");
        }
    }
}
