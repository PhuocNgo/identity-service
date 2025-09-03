package com.phuocngo.identity_service.controller;

import com.phuocngo.identity_service.dto.request.AuthRequest;
import com.phuocngo.identity_service.dto.request.TokenRequest;
import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.dto.response.AuthResponse;
import com.phuocngo.identity_service.enums.SuccessInfo;
import com.phuocngo.identity_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;

  @PostMapping("/log-in")
  public ResponseEntity<ApiResponse<AuthResponse>> logIn(@RequestBody AuthRequest authRequest) {
    AuthResponse authResponse = authService.logIn(authRequest);
    ApiResponse<AuthResponse> apiResponse =
        ApiResponse.<AuthResponse>builder().code(3000).result(authResponse).build();
    return ResponseEntity.accepted().body(apiResponse);
  }

  @PostMapping("/verify-token")
  public ResponseEntity<ApiResponse<AuthResponse>> verifyToken(@RequestBody TokenRequest token) {
    return ResponseEntity.ok()
        .body(
            ApiResponse.<AuthResponse>builder()
                .code(SuccessInfo.AUTHENTICATED.getCode())
                .message(SuccessInfo.AUTHENTICATED.getMessage())
                .result(authService.verifyToken(token.token()))
                .build());
  }
}
