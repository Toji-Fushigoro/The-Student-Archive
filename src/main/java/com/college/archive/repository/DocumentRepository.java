package com.college.archive.repository;

import com.college.archive.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    Page<Document> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Document> findBySubjectContainingIgnoreCase(String subject, Pageable pageable);
    
    Page<Document> findByCategory(Document.DocumentCategory category, Pageable pageable);
    
    Page<Document> findByYearAndSemester(Integer year, Integer semester, Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Document> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Document> findByUploadedBy_Id(Long userId);
}
