package com.skillgap.service;

import com.skillgap.dto.AuthResponse;
import com.skillgap.dto.LoginRequest;
import com.skillgap.dto.RegisterRequest;
import com.skillgap.entity.User;
import com.skillgap.repository.UserRepository;
import com.skillgap.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT");

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getId());
    }

    @Value("${google.client.id}")
    private String googleClientId;

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Get user details
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getId());
    }

    public AuthResponse verifyGoogleTokenAndLogin(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract email and name
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                
                // If name is null, use part of the email as username fallback
                String username = name != null ? name.replace(" ", "") : email.split("@")[0];

                // Check if user exists by email
                Optional<User> optionalUser = userRepository.findByEmail(email);
                User user;

                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                } else {
                    // Register new user via Google
                    user = new User();
                    
                    // Deduplicate username if needed
                    String baseUsername = username;
                    int counter = 1;
                    while (userRepository.existsByUsername(username)) {
                        username = baseUsername + counter;
                        counter++;
                    }
                    
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode("GOOGLE_OAUTH_LOGIN")); // Dummy password since they login with Google
                    user.setRole("STUDENT");
                    user = userRepository.save(user);
                }

                // Generate our application JWT token for the user
                String token = jwtUtil.generateToken(user.getUsername());
                return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getId());
            } else {
                throw new RuntimeException("Invalid Google ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token: " + e.getMessage());
        }
    }
}
