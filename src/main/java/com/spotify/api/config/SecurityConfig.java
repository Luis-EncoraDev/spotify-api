package com.spotify.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/oauthTokens/createTokens").ignoringRequestMatchers("/h2-console/**"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/oauthTokens/createTokens").authenticated()
                        .anyRequest().authenticated()
                ).headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}


