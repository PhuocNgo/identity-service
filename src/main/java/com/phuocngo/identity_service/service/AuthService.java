package com.phuocngo.identity_service.service;

import com.nimbusds.jose.*;
import com.phuocngo.identity_service.dto.request.AuthRequest;
import com.phuocngo.identity_service.dto.response.AuthResponse;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.repository.UserRepository;
import com.phuocngo.identity_service.security.JwtProvider;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;
  JwtProvider jwtProvider;

  public AuthResponse logIn(AuthRequest authRequest) {
    String rawPassword = authRequest.getPassword();

    var user =
        userRepository
            .findByUsername(authRequest.getUsername())
            .orElseThrow(() -> new UserException(ErrorInfo.USER_NOT_EXISTED));

    if (!passwordEncoder.matches(rawPassword, user.getPassword()))
      throw new UserException(ErrorInfo.UNSUCCESSFUL_LOGIN);

    String token = jwtProvider.generateToken(user, roleBuilder(user));
    return AuthResponse.builder().success(true).token(token).build();
  }

  public AuthResponse verifyToken(String token) {
    boolean isValid = jwtProvider.validateToken(token);
    return AuthResponse.builder().token(token).success(isValid).build();
  }

  public void logOut(String token) {
    jwtProvider.revokeToken(token);
  }

  private String roleBuilder(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");

    var roles = user.getRoles();

    roles.forEach(
        role -> {
          stringJoiner.add("ROLE_" + role.getName());
          role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        });
    return stringJoiner.toString();
  }
}
