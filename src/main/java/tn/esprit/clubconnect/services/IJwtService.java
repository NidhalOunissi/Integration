package tn.esprit.clubconnect.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface IJwtService {

    String extractUsername (String token);
    Claims extractAllClaims (String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateRefreshToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaimes, UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
    Boolean isTokenExpired(String token);




}
