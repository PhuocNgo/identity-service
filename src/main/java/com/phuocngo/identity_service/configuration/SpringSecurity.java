package com.phuocngo.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.security.CustomJwtDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringSecurity {
  final String[] publicApiPaths = {"/users", "/auth/log-in", "/auth/verify-token", "/auth/log-out"};
  final CustomJwtDecoder customJwtDecoder;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers(HttpMethod.POST, publicApiPaths)
                .permitAll()
                .anyRequest()
                .authenticated());

    httpSecurity.oauth2ResourceServer(
        oAuth2ResourceServerConfigurer -> {
          oAuth2ResourceServerConfigurer.jwt(
              jwtConfigurer -> {
                jwtConfigurer.decoder(customJwtDecoder);
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
              });
          oAuth2ResourceServerConfigurer.authenticationEntryPoint(
              (request, response, authException) -> {
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse<?> apiResponse =
                    ApiResponse.builder()
                        .code(ErrorInfo.UNAUTHENTICATED.getCode())
                        .message(authException.getMessage())
                        .build();

                response.setStatus(ErrorInfo.UNAUTHENTICATED.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
              });
        });

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
