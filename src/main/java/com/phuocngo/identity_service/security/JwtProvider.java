package com.phuocngo.identity_service.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phuocngo.identity_service.entity.RevokedToken;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.repository.RevokedTokenRepository;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("spring")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtProvider {
  RevokedTokenRepository revokedTokenRepository;

  @Value("${jwt.signerKey}")
  @NonFinal
  String signerKey;

  public String generateToken(User user, String roles) {
    JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .issuer("phuocngo")
            .subject(user.getUsername())
            .claim("scope", roles)
            .jwtID(UUID.randomUUID().toString())
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli()))
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwtHeader, payload);
    try {
      jwsObject.sign(new MACSigner(signerKey));
    } catch (JOSEException e) {
      throw new RuntimeException("Cannot sign token:", e);
    }

    return jwsObject.serialize();
  }

  public SignedJWT parseToken(String token) {
    try {
      return SignedJWT.parse(token);
    } catch (ParseException exception) {
      throw new RuntimeException("Cannot parse token:", exception);
    }
  }

  public boolean validateToken(String token) {
    try {
      SignedJWT signedJWT = parseToken(token);
      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
      JWSVerifier jwsVerifier = new MACVerifier(signerKey);

      String jid = claimsSet.getJWTID();
      Date expirationTime = claimsSet.getExpirationTime();
      boolean isValid = signedJWT.verify(jwsVerifier);

      Optional<RevokedToken> revokedToken = revokedTokenRepository.findById(jid);

      return expirationTime.after(new Date()) && isValid && revokedToken.isEmpty();
    } catch (ParseException | JOSEException exception) {
      throw new RuntimeException("Cannot validate token:", exception);
    }
  }

  public void revokeToken(String token) {
    try {
      SignedJWT signedJWT = parseToken(token);
      String jid = signedJWT.getJWTClaimsSet().getJWTID();
      Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

      RevokedToken revokedToken =
          RevokedToken.builder().tokenId(jid).expirationTime(expirationTime).build();
      revokedTokenRepository.save(revokedToken);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
