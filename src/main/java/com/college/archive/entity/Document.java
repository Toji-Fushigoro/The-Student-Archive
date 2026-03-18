package com.college.archive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;
    
    @Column(nullable = false)
    private String subject;
    
    @Column
    private Integer year;
    
    @Column
    private Integer semester;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;
    
    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    private String filename;
    
    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum DocumentCategory {
        PREVIOUS_YEAR_QUESTIONS,
        NOTES,
        STUDY_MATERIAL,
        OTHER
    }
}
