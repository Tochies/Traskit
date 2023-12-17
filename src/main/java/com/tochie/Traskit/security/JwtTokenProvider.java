package com.tochie.Traskit.security;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtTokenProvider {

    private String secretKey = "yourSecretKey"; // Replace with a secure random key
    private long validityInMilliseconds = 3600000; // 1 hour

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


}

