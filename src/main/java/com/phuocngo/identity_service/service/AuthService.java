package com.phuocngo.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phuocngo.identity_service.dto.request.AuthRequest;
import com.phuocngo.identity_service.dto.response.AuthResponse;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.repository.UserRepository;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;

  @NonFinal
  @Value("${jwt.signerKey}")
  String secretKey;

  public AuthResponse logIn(AuthRequest authRequest) {
    String rawPassword = authRequest.getPassword();

    var user =
        userRepository
            .findByUsername(authRequest.getUsername())
            .orElseThrow(() -> new UserException(ErrorInfo.USER_NOT_EXISTED));

    if (!passwordEncoder.matches(rawPassword, user.getPassword()))
      throw new UserException(ErrorInfo.UNSUCCESSFUL_LOGIN);

    String token = generateToken(user);
    return AuthResponse.builder().success(true).token(token).build();
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
    JWTClaimsSet claimsSet =
        new JWTClaimsSet.Builder()
            .issuer("phuocngo")
            .subject(user.getUsername())
            .claim("scope", roleBuilder(user))
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli()))
            .build();

    Payload payload = new Payload(claimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    try {
      jwsObject.sign(new MACSigner(secretKey));
    } catch (JOSEException exception) {
      log.error(exception.getMessage());
      throw new RuntimeException(exception);
    }

    return jwsObject.serialize();
  }

  public AuthResponse verifyToken(String token) {
    try {
      JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
      SignedJWT signedJWT = SignedJWT.parse(token);
      Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
      boolean isValid = signedJWT.verify(verifier);

      if (!isValid || expirationTime.before(new Date())) {
        throw new UserException(ErrorInfo.INVALID_TOKEN);
      }

    } catch (JOSEException | ParseException exception) {
      log.error(exception.getMessage());
      throw new RuntimeException(exception);
    }

    return AuthResponse.builder().token(token).success(true).build();
  }

  private String roleBuilder(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    var roles = user.getRoles();

    roles.forEach(stringJoiner::add);
    return stringJoiner.toString();
  }
}
