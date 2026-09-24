package com.example.accountdept.security;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expirationMs}")
    private long expirationMs;

    @Value("${app.jwt.rememberExpirationMs}")
    private long rememberExpirationMs;

    public String generateToken(String username, String role, boolean rememberMe) {
        long exp = rememberMe ? rememberExpirationMs : expirationMs;
        Date now = new Date();
        Date expiry = new Date(now.getTime() + exp);
        if (jwtSecret.getBytes().length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes) for HS256 algorithm.");
        }
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Jws<io.jsonwebtoken.Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return jws.getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Jws<io.jsonwebtoken.Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Object r = jws.getBody().get("role");
        return r == null ? null : r.toString();
    }
}