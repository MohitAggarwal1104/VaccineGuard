package com.vaccine.VaccinationReminderSystem.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaccine.VaccinationReminderSystem.model.User;
import com.vaccine.VaccinationReminderSystem.repository.UserRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Registers a new user.
     * @param user The user object containing name, email, and plain-text password.
     * @return The saved user.
     */
    public User signup(User user) {
        // Check if user with this email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * Authenticates a user and returns a JWT token.
     * @param email The user's email.
     * @param password The user's plain-text password.
     * @return A JWT token string.
     */
    public String login(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Check if the provided password matches the stored hashed password
        if (passwordEncoder.matches(password, user.getPassword())) {
            // If passwords match, generate and return a JWT token
            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        } else {
            // If passwords do not match, throw an exception
            throw new RuntimeException("Invalid credentials");
        }
    }

    public Map<String, String> generateTokenMap(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}