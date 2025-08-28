package com.phuocngo.identity_service.exception;

import com.phuocngo.identity_service.enums.ErrorInfo;

public class UserException extends RuntimeException {
    private final ErrorInfo errorInfo;

    public UserException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
