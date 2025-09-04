package com.phuocngo.identity_service.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorInfo {
  UNCATEGORIZED_ERROR(9999, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
  USER_EXISTED(1001, "User has been existed.", HttpStatus.CONFLICT),
  INVALID_USERNAME(1002, "Your username must be at least 3 characters.", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(1003, "Your password must be at least 8 characters.", HttpStatus.BAD_REQUEST),
  USER_NOT_EXISTED(1004, "Username has not been existed.", HttpStatus.NOT_FOUND),
  UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  INVALID_TOKEN(1006, "Your token is invalid.", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(1007, "You have no permission", HttpStatus.FORBIDDEN),
  UNSUCCESSFUL_LOGIN(1008, "Your username or password is incorrect.", HttpStatus.UNAUTHORIZED);
  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;

  ErrorInfo(int code, String message, HttpStatusCode statusCode) {
    this.message = message;
    this.code = code;
    this.statusCode = statusCode;
  }
}
