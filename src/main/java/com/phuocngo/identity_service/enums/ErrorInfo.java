package com.phuocngo.identity_service.enums;

import lombok.Getter;

@Getter
public enum ErrorInfo {
  UNCATEGORIZED_ERROR(9999, "Uncategorized error."),
  USER_EXISTED(1001, "User has been existed."),
  INVALID_USERNAME(1002, "Your username must be at least 3 characters."),
  INVALID_PASSWORD(1003, "Your password must be at least 8 characters."),
  USER_NOT_EXISTED(1004, "Username has not been existed."),
  UNAUTHENTICATED(1005, "Your username or password is incorrect."),
  INVALID_TOKEN(1006, "Your token is invalid.");
  private final int code;
  private final String message;

  ErrorInfo(int code, String message) {
    this.message = message;
    this.code = code;
  }
}
