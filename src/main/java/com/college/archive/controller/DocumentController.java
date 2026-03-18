package com.college.archive.controller;

import com.college.archive.dto.DocumentUploadRequest;
import com.college.archive.entity.Document;
import com.college.archive.entity.User;
import com.college.archive.service.DocumentService;
import com.college.archive.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    
    private final DocumentService documentService;
    private final UserService userService;
    
    @GetMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String uploadPage(Authentication authentication, Model model) {
        User currentUser = userService.getCurrentUser(authentication);
        model.addAttribute("user", currentUser);
        model.addAttribute("categories", Document.DocumentCategory.values());
        return "upload";
    }
    
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                @ModelAttribute DocumentUploadRequest request,
                                Authentication authentication,
                                Model model) {
        try {
            User currentUser = userService.getCurrentUser(authentication);
            Document document = documentService.uploadDocument(file, request, currentUser);
            model.addAttribute("success", "Document uploaded successfully: " + document.getTitle());
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Error uploading document", e);
            model.addAttribute("error", "Failed to upload document: " + e.getMessage());
            model.addAttribute("categories", Document.DocumentCategory.values());
            return "upload";
        }
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        try {
            Document document = documentService.getDocument(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", document.getFilename());
            headers.setContentLength(document.getFileSize());
            
            return new ResponseEntity<>(document.getFileData(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error downloading document", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDocument(@PathVariable Long id, 
                                Authentication authentication,
                                Model model) {
        try {
            User currentUser = userService.getCurrentUser(authentication);
            documentService.deleteDocument(id, currentUser);
            model.addAttribute("success", "Document deleted successfully");
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Error deleting document", e);
            model.addAttribute("error", "Failed to delete document: " + e.getMessage());
            return "redirect:/home";
        }
    }
}
