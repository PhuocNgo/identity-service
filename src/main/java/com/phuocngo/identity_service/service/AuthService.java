package com.phuocngo.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phuocngo.identity_service.dto.payload.UserPayload;
import com.phuocngo.identity_service.dto.request.AuthRequest;
import com.phuocngo.identity_service.dto.response.AuthResponse;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.mapper.UserMapper;
import com.phuocngo.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    String secretKey;

    public AuthResponse logIn(AuthRequest authRequest) {
        String rawPassword = authRequest.getPassword();


       var user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserException(ErrorInfo.USER_NOT_EXISTED));

       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       if (!passwordEncoder.matches(rawPassword, user.getPassword()))
            throw new UserException(ErrorInfo.UNAUTHENTICATED);

       UserPayload userPayload = userMapper.toUserPayload(user);

       String token = generateToken(userPayload);
       return AuthResponse.builder()
                .success(true)
                .token(token)
                .build();
    }

    public String generateToken(UserPayload userPayload) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("phuocngo")
                .claim("userPayload", userPayload.toString())
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli())
                ).build();

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


        return AuthResponse.builder()
                .token(token)
                .success(true)
                .build();
    }
}
