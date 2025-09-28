package com.vaccine.management.controller;

import com.vaccine.management.dto.AuthenticationRequest;
import com.vaccine.management.dto.AuthenticationResponse;
import com.vaccine.management.dto.RegisterRequest;
import com.vaccine.management.model.AuthProvider;
import com.vaccine.management.model.Role;
import com.vaccine.management.model.User;
import com.vaccine.management.repository.UserRepository;
import com.vaccine.management.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest) {
        log.info("Processing registration request for email: {}", registerRequest.getEmail());

        if (userRepository.existsByUsername(registerRequest.getEmail())) {
            log.warn("Registration failed: Username (email) already exists: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email is already registered."));
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getEmail());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL); // Set auth provider for local registration

        try {
            Role userRole = Role.valueOf("ROLE_" + registerRequest.getRole().toUpperCase());
            user.setRoles(Collections.singleton(userRole));
        } catch (IllegalArgumentException e) {
            log.error("Invalid role provided during registration: {}", registerRequest.getRole());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid role specified."));
        }

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        // Return a JSON object for a consistent API response
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Processing login request for user: {}", authenticationRequest.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

