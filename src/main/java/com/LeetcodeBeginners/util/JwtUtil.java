package com.LeetcodeBeginners.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Automatically generates a secure key
    private final long expirationTimeMs = 24 * 60 * 60 * 1000; // 24 hours

    // Generate JWT Token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(key) // Use the Key object for signing
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token is valid.");
            return true;
        } catch (JwtException e) {
            // Log the exception or handle it as needed
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    // Extract Email from JWT Token
    public String extractEmail(String token) {
        String email = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("Extracted email: " + email);
        return email;
    }

    // Extract Role from JWT Token
    public String extractRole(String token) {
        String role = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        System.out.println("Extracted role: " + role);
        return role;
    }
}
