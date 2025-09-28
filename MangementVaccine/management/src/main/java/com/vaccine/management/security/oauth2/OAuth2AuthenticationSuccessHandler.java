package com.vaccine.management.security.oauth2;

import com.vaccine.management.model.AuthProvider;
import com.vaccine.management.model.Role;
import com.vaccine.management.model.User;
import com.vaccine.management.repository.UserRepository;
import com.vaccine.management.security.JwtUtil;
import com.vaccine.management.security.MyUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);


    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("OAuth2 success for user email: {}", email);

        // Find or create the user
        User user = userRepository.findByUsername(email).orElseGet(() -> {
            log.info("User not found, creating a new user for email: {}", email);
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            // Default new Google sign-ups to the PARENT role
            newUser.setRoles(Collections.singleton(Role.ROLE_PARENT));
            return userRepository.save(newUser);
        });

        // Create UserDetails to generate the token
        UserDetails userDetails = new MyUserDetailsService.CustomUserDetails(user);

        String token = jwtUtil.generateToken(userDetails);
        
        // The target URL to redirect to on the frontend
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/")
                .queryParam("token", token)
                .build().toUriString();
        
        log.info("Redirecting user {} to target URL with token", email);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

