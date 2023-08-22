package com.api.authapi.services;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${app.secretKey}")
    private String secretKey;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwt(String userId) {
        return Jwts
            .builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + this.jwtExpirationMs))
            .signWith(this.getSecretKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    public String validateJwt(String jwt) {
        Jwts.parserBuilder()
            .setSigningKey(this.getSecretKey())
            .build()
            .parseClaimsJws(jwt);

        return Jwts.parserBuilder()
            .setSigningKey(this.getSecretKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody()
            .getSubject();
    }

    public Key getSecretKey() {
        System.out.println(this.secretKey);
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }
}
