package com.example.pepelingbackendv2.configs;

import com.example.pepelingbackendv2.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    public String generateToken(UserDetails user)
    {
        Date now = new Date();
        return Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret).compact();
    }
    public String getUsernameFrom(String token)
    {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody()
                .getSubject();
    }
    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
