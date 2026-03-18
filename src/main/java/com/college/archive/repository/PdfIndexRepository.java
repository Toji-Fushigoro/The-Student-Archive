package com.college.archive.repository;

import com.college.archive.entity.PdfIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfIndexRepository extends JpaRepository<PdfIndex, Long> {
    
    Optional<PdfIndex> findByDocument_Id(Long documentId);
    
    @Query("SELECT pi FROM PdfIndex pi WHERE " +
           "LOWER(pi.extractedText) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(pi.tokenizedKeywords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PdfIndex> searchByKeyword(@Param("keyword") String keyword);
    
    void deleteByDocument_Id(Long documentId);
}
