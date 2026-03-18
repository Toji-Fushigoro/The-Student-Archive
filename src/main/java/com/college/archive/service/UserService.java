package com.college.archive.service;

import com.college.archive.entity.User;
import com.college.archive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttribute("email");
        
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
    
    @Transactional
    public User updateUserRole(Long userId, User.UserRole role) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setRole(role);
        log.info("Updated user {} role to {}", user.getEmail(), role);
        return userRepository.save(user);
    }
    
    @Transactional
    public User updateUserProfile(Long userId, String name) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setName(name);
        log.info("Updated user {} profile", user.getEmail());
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public boolean hasRole(User user, User.UserRole role) {
        return user.getRole() == role;
    }
    
    @Transactional(readOnly = true)
    public boolean canUpload(User user) {
        return user.getRole() == User.UserRole.TEACHER || 
               user.getRole() == User.UserRole.ADMIN;
    }
    
    @Transactional(readOnly = true)
    public boolean isAdmin(User user) {
        return user.getRole() == User.UserRole.ADMIN;
    }
}
