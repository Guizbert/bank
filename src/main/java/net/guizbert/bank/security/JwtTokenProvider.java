package net.guizbert.bank.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Injecting the JWT secret key from application properties
    @Value("${jwt-secret}")
    private String token;

    // Injecting the token expiration time (in milliseconds) from application properties
    @Value("${jwt-exp}")
    private long tokenExpiration;


    /**
     * Generates a JWT token based on the authentication object.
     * The token will contain the username (subject), issue date, and expiration date.
     *
     * @param auth The authentication object that contains user details.
     * @return A compact JWT token as a string.
     */
    public String generateToken(Authentication auth)
    {
        String username = auth.getName();// Get the username (subject) from authentication
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + tokenExpiration);

        // Generate and return the JWT token with the username, issue date, and expiration date
        return Jwts.builder()
                .subject(username)
                .issuedAt(today)
                .expiration(expirationDate)
                .signWith(key()) // Sign the JWT with the HMAC key
                .compact(); // Generate the compact string representation of the token
    }


    /**
     * Creates a Key object for HMAC signing from the base64 encoded secret key.
     *
     * @return The Key object to use for signing and verifying JWT tokens.
     */
    Key key()
    {
        byte[] bytes = Decoders.BASE64.decode(token);
        return Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Retrieves the username (subject) from the JWT token by parsing it and verifying the signature.
     *
     * @param name The JWT token to extract the username from.
     * @return The username (subject) contained in the token.
     */
    public String getUserName(String name)
    {
        // Parse the JWT token and extract the claims (payload)
        Claims claims = Jwts.parser()
                .setSigningKey((key()))
                .build()
                .parseClaimsJws(name)
                .getPayload();

        // Return the username from the claims
        return claims.getSubject();
    }

    /**
     * Validates the JWT token by checking its signature and expiration date.
     * If the token is valid, it returns true. Otherwise, it throws an exception.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, otherwise an exception is thrown.
     */
    public boolean validateToken(String token)
    {
        try {
            // Validate the token by parsing it with the signing key
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }


}
