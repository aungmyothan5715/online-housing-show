package com.example.backendapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "T91HgwvdglSwVf7WK1xmL5C3HrWGwKmj+aLcRsbDN0Ou7TgsLXWAiMRMNwhweVX+";
    private static final long EXPIRATION_TIME = 864_000_000; //10 days

    public String generateToken(String username) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getUserFormToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
