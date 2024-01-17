package com.shxnzxxn.book.springboot.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shxnzxxn.book.springboot.domain.user.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.PrintWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable());
        http.headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.requestMatchers(PathRequest.toH2Console()).permitAll());
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.requestMatchers(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/images/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/h2-console/**")
        ).permitAll());
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.requestMatchers("/api/v1/**").hasRole(Role.USER.name()));
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().authenticated());
        http.logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"));
        http.oauth2Login((oauth2LoginConfig) ->
                oauth2LoginConfig.userInfoEndpoint((userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(customOAuth2UserService))
                )
        );
        http.exceptionHandling((exceptionConfig) ->
                exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler((accessDeniedHandler)));

        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Spring security unauthorized...");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Spring security forbidden...");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {

        private final HttpStatus status;
        private final String message;
    }

}
