package com.college.archive.controller;

import com.college.archive.dto.DocumentSearchResult;
import com.college.archive.entity.User;
import com.college.archive.service.SearchService;
import com.college.archive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchService searchService;
    private final UserService userService;
    
    @GetMapping
    public String search(@RequestParam(required = false) String query,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size,
                        Authentication authentication,
                        Model model) {
        
        User currentUser = userService.getCurrentUser(authentication);
        model.addAttribute("user", currentUser);
        
        if (query != null && !query.trim().isEmpty()) {
            List<DocumentSearchResult> results = searchService.search(
                query, 
                currentUser, 
                PageRequest.of(page, size)
            );
            model.addAttribute("results", results);
            model.addAttribute("query", query);
            model.addAttribute("totalResults", results.size());
        }
        
        return "search";
    }
}
