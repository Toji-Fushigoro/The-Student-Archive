package com.college.archive.controller;

import com.college.archive.entity.User;
import com.college.archive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public String profile(Authentication authentication, Model model) {
        User currentUser = userService.getCurrentUser(authentication);
        model.addAttribute("user", currentUser);
        return "profile";
    }
    
    @PostMapping("/update-role")
    public String updateRole(@RequestParam User.UserRole role,
                            Authentication authentication,
                            Model model) {
        try {
            User currentUser = userService.getCurrentUser(authentication);
            userService.updateUserRole(currentUser.getId(), role);
            model.addAttribute("success", "Role updated successfully");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update role: " + e.getMessage());
            return "redirect:/profile";
        }
    }
}
