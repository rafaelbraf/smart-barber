package com.optimiza.clickbarber.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        return criarToken(claims, email);
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String extraiEmail(String token) {
        var claims = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getPayload();

        return claims.getSubject();
    }

    private String criarToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

}
