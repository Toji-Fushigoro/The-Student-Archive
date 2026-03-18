package com.college.archive.service;

import com.college.archive.entity.User;
import com.college.archive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = extractEmail(registrationId, attributes);
        String name = extractName(registrationId, attributes);
        String oauthId = extractOAuthId(registrationId, attributes);
        
        log.info("OAuth2 user loaded - Provider: {}, Email: {}, Name: {}", registrationId, email, name);
        
        User user = userRepository.findByOauthProviderAndOauthId(registrationId, oauthId)
            .orElseGet(() -> {
                log.info("Creating new user for OAuth ID: {}", oauthId);
                User newUser = User.builder()
                    .oauthProvider(registrationId)
                    .oauthId(oauthId)
                    .email(email)
                    .name(name)
                    .role(User.UserRole.STUDENT)
                    .build();
                return userRepository.save(newUser);
            });
        
        String nameAttributeKey = getNameAttributeKey(registrationId);
        
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
            attributes,
            nameAttributeKey
        );
    }
    
    private String extractEmail(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("email");
        } else if ("github".equals(registrationId)) {
            return (String) attributes.get("email");
        }
        throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
    }
    
    private String extractName(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("name");
        } else if ("github".equals(registrationId)) {
            return (String) attributes.get("name");
        }
        throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
    }
    
    private String extractOAuthId(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("sub");
        } else if ("github".equals(registrationId)) {
            return String.valueOf(attributes.get("id"));
        }
        throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
    }
    
    private String getNameAttributeKey(String registrationId) {
        if ("google".equals(registrationId)) {
            return "sub";
        } else if ("github".equals(registrationId)) {
            return "id";
        }
        return "id";
    }
}
