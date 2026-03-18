package com.college.archive.dto;

import com.college.archive.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadRequest {
    private String title;
    private String description;
    private Document.DocumentCategory category;
    private String subject;
    private Integer year;
    private Integer semester;
}
