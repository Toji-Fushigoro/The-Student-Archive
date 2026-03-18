package com.college.archive.dto;

import com.college.archive.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSearchResult {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String subject;
    private Integer year;
    private Integer semester;
    private String filename;
    private Long fileSize;
    private String uploaderName;
    private LocalDateTime createdAt;
    private Double relevanceScore;
    
    public static DocumentSearchResult fromDocument(Document document, Double relevanceScore) {
        return DocumentSearchResult.builder()
            .id(document.getId())
            .title(document.getTitle())
            .description(document.getDescription())
            .category(document.getCategory().name())
            .subject(document.getSubject())
            .year(document.getYear())
            .semester(document.getSemester())
            .filename(document.getFilename())
            .fileSize(document.getFileSize())
            .uploaderName(document.getUploadedBy().getName())
            .createdAt(document.getCreatedAt())
            .relevanceScore(relevanceScore)
            .build();
    }
}
