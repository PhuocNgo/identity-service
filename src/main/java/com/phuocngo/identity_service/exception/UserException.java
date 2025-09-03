package com.phuocngo.identity_service.exception;

import com.phuocngo.identity_service.enums.ErrorInfo;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
  private final ErrorInfo errorInfo;

  public UserException(ErrorInfo errorInfo) {
    super(errorInfo.getMessage());
    this.errorInfo = errorInfo;
  }
}
