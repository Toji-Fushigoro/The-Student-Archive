package com.college.archive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "search_query", nullable = false)
    private String searchQuery;
    
    @Column(name = "results_count")
    private Integer resultsCount;
    
    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;
    
    @PrePersist
    protected void onCreate() {
        searchedAt = LocalDateTime.now();
    }
}
