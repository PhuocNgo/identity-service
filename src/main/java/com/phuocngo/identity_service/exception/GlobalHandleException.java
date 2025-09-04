package com.phuocngo.identity_service.exception;

import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.enums.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException() {
    ApiResponse<?> apiResponse =
        ApiResponse.builder()
            .code(ErrorInfo.UNCATEGORIZED_ERROR.getCode())
            .message(ErrorInfo.UNCATEGORIZED_ERROR.getMessage())
            .build();
    return ResponseEntity.status(ErrorInfo.UNCATEGORIZED_ERROR.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException runtimeException) {
    ApiResponse<?> apiResponse =
        ApiResponse.builder().code(1001).message(runtimeException.getMessage()).build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ApiResponse<?>> handleUserException(UserException userException) {
    ApiResponse<?> apiResponse =
        ApiResponse.builder()
            .code(userException.getErrorInfo().getCode())
            .message(userException.getErrorInfo().getMessage())
            .build();
    return ResponseEntity.status(userException.getErrorInfo().getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidateException(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    ErrorInfo errorInfo =
        ErrorInfo.valueOf(methodArgumentNotValidException.getFieldError().getDefaultMessage());
    ApiResponse<?> apiResponse =
        ApiResponse.builder().message(errorInfo.getMessage()).code(errorInfo.getCode()).build();

    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(
      AccessDeniedException exception) {
    ApiResponse<?> apiResponse =
        ApiResponse.builder()
            .message(exception.getMessage())
            .code(ErrorInfo.UNAUTHORIZED.getCode())
            .build();
    return ResponseEntity.status(ErrorInfo.UNAUTHORIZED.getStatusCode()).body(apiResponse);
  }
}
