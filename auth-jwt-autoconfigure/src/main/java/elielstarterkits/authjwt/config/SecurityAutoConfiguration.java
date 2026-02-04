package elielstarterkits.authjwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import elielstarterkits.authjwt.properties.JwtProperties;
import elielstarterkits.authjwt.security.JwtAuthenticationEntryPoint;
import elielstarterkits.authjwt.security.JwtAuthenticationFilter;
import elielstarterkits.authjwt.token.JwtTokenProvider;
import elielstarterkits.authjwt.token.TokenBlacklistService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

@Configuration
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtProperties props,
            JwtTokenProvider tokenProvider,
            Optional<TokenBlacklistService> blacklistService
    ) {
        return new JwtAuthenticationFilter(props, tokenProvider, blacklistService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationEntryPoint entryPoint,
            JwtAuthenticationFilter jwtFilter
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
            .authorizeHttpRequests(auth -> auth
                // endpoints publics (phase 2)
                .requestMatchers("/auth/login", "/auth/refresh", "/auth/logout").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
