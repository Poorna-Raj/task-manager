package com.abbys.tms.service;

import com.abbys.tms.data.user.dto.TokenPair;
import com.abbys.tms.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    @Value("${app.jwt.secret}") private String jwtSecret;
    @Value("${app.jwt.expiration}") private Long jwtExpirationMs;
    @Value("${app.jwt.refresh-expiration}") private Long refreshExpirationMs;

    public String generateRefreshToken(Authentication auth) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("tokenType","refresh");

        return generateToken(auth,refreshExpirationMs,claims);
    }

    public String generateAccessToken(Authentication auth) {
        return generateToken(auth,jwtExpirationMs,new HashMap<>());
    }

    private String generateToken(Authentication auth, Long expirationMs, Map<String, Object> claims) {
        UserDetails userPrincipal = (UserDetails) auth.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        assert userPrincipal != null;
        return Jwts.builder()
                .header()
                .add("type","JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateTokenForUser(String token,UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);

        if(claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);

        if(claims == null) {
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;

        try{
            claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch(JwtException | IllegalArgumentException e) {
            throw new JwtValidationException(e.getMessage());
        }
        return claims;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenPair generateTokenPair(Authentication auth) {
        String accessToken = generateAccessToken(auth);
        String refreshToken = generateRefreshToken(auth);

        return new TokenPair(accessToken,refreshToken);
    }
}
