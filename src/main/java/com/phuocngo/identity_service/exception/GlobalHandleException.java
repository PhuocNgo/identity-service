package com.phuocngo.identity_service.exception;

import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.enums.ErrorInfo;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
    ApiResponse<?> apiResponse =
        ApiResponse.builder()
            .code(ErrorInfo.UNCATEGORIZED_ERROR.getCode())
            .message(e.getMessage())
            .build();
    return ResponseEntity.status(ErrorInfo.UNCATEGORIZED_ERROR.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<?>> handleException(IllegalArgumentException e) {
    return ResponseEntity.status(ErrorInfo.UNCATEGORIZED_ERROR.getStatusCode())
        .body(
            ApiResponse.builder()
                .code(ErrorInfo.UNCATEGORIZED_ERROR.getCode())
                .message(e.getMessage())
                .build());
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
    String errorInfoKey =
        Objects.requireNonNull(methodArgumentNotValidException.getFieldError()).getDefaultMessage();
    ErrorInfo errorInfo = ErrorInfo.valueOf(errorInfoKey);

    var constraintViolation =
        methodArgumentNotValidException
            .getBindingResult()
            .getAllErrors()
            .getFirst()
            .unwrap(ConstraintViolation.class);
    var attributes = constraintViolation.getConstraintDescriptor().getAttributes();
    String newMessage = mappingAttributes(attributes, errorInfo.getMessage());

    ApiResponse<?> apiResponse =
        ApiResponse.builder().message(newMessage).code(errorInfo.getCode()).build();

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

  private String mappingAttributes(Map<String, Object> attributes, String message) {
    String min = "min";
    var attributeVal = String.valueOf(attributes.get(min));
    return message.replace("{" + min + "}", attributeVal);
  }
}
