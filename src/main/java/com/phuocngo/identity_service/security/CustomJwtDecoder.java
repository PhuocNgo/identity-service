package com.phuocngo.identity_service.security;

import com.phuocngo.identity_service.repository.RevokedTokenRepository;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
  final RevokedTokenRepository revokedTokenRepository;

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Override
  public Jwt decode(String token) {
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
    NimbusJwtDecoder nimbusJwtDecoder =
        NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();

    nimbusJwtDecoder.setJwtValidator(
        token1 -> {
          String jti = token1.getId();
          if (revokedTokenRepository.existsById(jti)) {
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("Unauthenticated", "Token has been revoked", null));
          }

          return OAuth2TokenValidatorResult.success();
        });

    return nimbusJwtDecoder.decode(token);
  }
}
