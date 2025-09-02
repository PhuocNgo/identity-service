package com.phuocngo.identity_service.configuration;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
  private final String[] publicApiPaths = {"/users", "/auth/log-in"};

  @Value("${jwt.signerKey}")
  private String signerKey;

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
        oAuth2ResourceServerConfigurer ->
            oAuth2ResourceServerConfigurer.jwt(
                jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
    return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
  }

}
