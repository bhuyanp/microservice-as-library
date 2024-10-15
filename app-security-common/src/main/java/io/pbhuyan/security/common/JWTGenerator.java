package io.pbhuyan.security.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String AUTHORITIES = "Authorities";

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Instant currentInstant = Instant.now();
        Instant expireInstant = currentInstant.plusSeconds(36000);
        Date expireDate = Date.from(expireInstant);

        String token =
                Jwts.builder()
                        .setSubject(username)
//                        .setClaims(Map.of(AUTHORITIES, authentication.getAuthorities().stream()
//                                .map(GrantedAuthority::getAuthority).toList()))
                        .setIssuedAt(new Date())
                        .setExpiration(expireDate)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact();
        System.out.println("New token : ");
        System.out.println(token);
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException(
                    "JWT was expired or incorrect", ex.fillInStackTrace());
        }
    }
}