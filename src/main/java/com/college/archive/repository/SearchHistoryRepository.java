package com.college.archive.repository;

import com.college.archive.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    
    List<SearchHistory> findByUser_IdOrderBySearchedAtDesc(Long userId);
    
    List<SearchHistory> findTop10ByUser_IdOrderBySearchedAtDesc(Long userId);
}
