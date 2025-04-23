package com.booking.app.utils;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtUtils {
  private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; // 7 days

  private final SecretKey Key;

  public JwtUtils() {
    String base64Secret = "Gm5BfH6v9rXQ4KsH7KDUpiyMA6zrMwJXK6SRJ3MFLz0=";
    byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
    this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(Key)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
    return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
  }

  public boolean isValidToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractClaims(token, Claims::getExpiration).before(new Date());
  }
}