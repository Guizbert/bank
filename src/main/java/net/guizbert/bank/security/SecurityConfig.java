package net.guizbert.bank.security;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor // This annotation generates a constructor with required dependencies for this class
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method security (e.g., @PreAuthorize)
public class SecurityConfig {

    private final UserDetailsService userDetailsService; // The service used to load user-specific data
    private final JwtAuthFilter jwtAuthFilter; // Custom JWT authentication filter

    // Bean to provide a password encoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // Bean to configure HTTP security, including CSRF protection, authorization, and JWT filter
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable()) // Disable CSRF protection (for stateless API)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, "/api/users") // Allow unauthenticated access to /api/users (user registration)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users/login") // Allow unauthenticated access to /api/users/login (user login)
                                .permitAll()
                                .anyRequest().authenticated() // All other requests require authentication
                );
        // Configure stateless session management (no server-side session)
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Set up
        httpSecurity.authenticationProvider(authProvider());
        // Add JWT filter before the UsernamePasswordAuthenticationFilter (to handle JWT validation)
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // Bean to provide an AuthenticationManager to be used for authentication
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean to configure the custom authentication provider with UserDetailsService and PasswordEncoder
    @Bean
    public AuthenticationProvider authProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
