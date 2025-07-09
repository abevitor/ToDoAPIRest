package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION = 8640000;

    public String gerarToken(String email) {
        return Jwts.builder()
               .setSubject(email)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION))
               .signWith(key)
               .compact();
               
    }

    public String getEmailToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public boolean validarToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
            } catch (JwtException e) {
                return false;
            }
        }
    }
    

