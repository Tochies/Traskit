package com.tochie.Traskit.security;

import com.tochie.Traskit.exception.AuthException;
import com.tochie.Traskit.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Value("${security.jwt.token.expire-length:600000}")
    private long validityInMilliseconds = 600000; // 10mins

    @Value("${security.jwt.token.blacklist.enable:true}")
    private Boolean blacklistUsedToken = true;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private UserDetails userDetails;

    private ConcurrentHashMap<String, String> blacklistCache = new ConcurrentHashMap<>();


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        if(token == null) return null;
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token) {

        try {
            final String username = extractUsername(token);

            if(!validateTokenKey(token) | blacklistCache.containsKey(token) | username == null) throw new AuthException("api authentication failed");

            if(blacklistUsedToken) blacklistCache.put(token, username);


            userDetails = getUserDetails(username);

            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){
            log.warn("JWTException: {}", e.getMessage());
            throw new AuthException("api authentication failed");
        }


    }

    public boolean validateTokenKey(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWTException: {}", e.getMessage());
            throw new AuthException("api authentication failed");
        }

        return true;

    }

    public Authentication getAuthentication(HttpServletResponse response, HttpServletRequest request) {

        request.setAttribute("user", userDetails);

        String jwt = "Bearer ".concat(generateToken(userDetails.getUsername()));

        response.addHeader("Authorization" , jwt);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    private UserDetails getUserDetails(String username) {

        return userDetailsService.loadUserByUsername(username);
    }


}
