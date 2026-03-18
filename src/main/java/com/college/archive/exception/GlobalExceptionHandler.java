package com.college.archive.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DocumentNotFoundException.class)
    public String handleDocumentNotFound(DocumentNotFoundException ex, Model model) {
        log.error("Document not found", ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
    
    @ExceptionHandler(FileUploadException.class)
    public String handleFileUpload(FileUploadException ex, Model model) {
        log.error("File upload error", ex);
        model.addAttribute("error", ex.getMessage());
        return "upload";
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex, Model model) {
        log.error("File size exceeded", ex);
        model.addAttribute("error", "File size exceeds maximum limit of 10MB");
        return "upload";
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException ex, Model model) {
        log.error("Unauthorized access", ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}
