package com.sankalp.quickbite.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final Duration expiration;

    public JwtService(SecretKey secretKey, Duration expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    public String createToken(Authentication authentication) {
        String sub = authentication.getName();
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

        List<String> authorities = grantedAuthorities
                                        .stream()
                                        .map(auth -> auth.getAuthority())
                                        .collect(Collectors.toList());

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expiration);

        return Jwts.builder()
                .subject(sub)
                .expiration(Date.from(expiresAt))
                .claim("authorities", authorities)
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parseAndValidateToken(String jwt) throws JwtException {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt);
    }
}
