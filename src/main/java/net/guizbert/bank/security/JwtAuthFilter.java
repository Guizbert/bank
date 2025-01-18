package net.guizbert.bank.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marks this class as a Spring-managed bean (component) that will be automatically registered in the context
public class JwtAuthFilter extends OncePerRequestFilter {


    private JwtTokenProvider jwtTokenprovider; // JWT token provider for generating/validating tokens
    private UserDetailsService userDetailsService; // UserDetailsService to load user data

    // This method is called for every incoming HTTP request, ensuring the JWT token is validated for each request
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extract the token from the request
        String token = getToken(request);

        // If the token is valid, authenticate the user
        if(StringUtils.hasText(token) && jwtTokenprovider.vaidateToken(token)) {
            // Extract the username from the token
            String userName = jwtTokenprovider.getUserName(token);

            // Load user details from the UserDetailsService using the username
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            // Create an authentication token with the user details and set it to the security context
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set the request details (e.g., IP address)
            SecurityContextHolder.getContext().setAuthentication(authToken); // Set the authentication token in the security context
        }

        // Continue the filter chain (allow the request to proceed)
        filterChain.doFilter(request, response);
    }

    // Helper method to extract the token from the HTTP request's "Auth" header
    private String getToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Auth");

        // If the header contains "Bearer " followed by the token, extract and return the token
        if(StringUtils.hasText(tokenHeader) && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7); // Remove the "Bearer " prefix and return the token
        }
        return null; // Return null if no valid token is found
    }
}
