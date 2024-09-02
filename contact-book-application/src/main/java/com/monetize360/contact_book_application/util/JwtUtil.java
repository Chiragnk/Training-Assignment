package com.monetize360.contact_book_application.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
    public class JwtUtil {

//        @Value("${jwt.secret}")
//        private String secretKey;
//
//        @Value("${jwt.expiration}")
//        private long expiration;
private final String secretKey = "yourSecretKeyHere"; // Replace with your actual secret key
    private final long expiration = 1000 * 60 * 60 * 10;

    public String generateToken(String username) {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }

        public boolean validateToken(String token, String username) {
            final String tokenUsername = getUsernameFromToken(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        }

        public String getUsernameFromToken(String token) {
            return getClaimsFromToken(token).getSubject();
        }

        private boolean isTokenExpired(String token) {
            return getClaimsFromToken(token).getExpiration().before(new Date());
        }

        private Claims getClaimsFromToken(String token) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }
}
