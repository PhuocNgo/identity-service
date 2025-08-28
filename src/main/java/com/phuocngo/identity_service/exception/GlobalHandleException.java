package com.phuocngo.identity_service.exception;

import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.enums.ErrorInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorInfo.UNCATEGORIZED_ERROR.getCode());
        apiResponse.setMessage(ErrorInfo.UNCATEGORIZED_ERROR.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException runtimeException) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(1001);
        apiResponse.setMessage(runtimeException.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> handleUserException(UserException userException) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(userException.getErrorInfo().getCode());
        apiResponse.setMessage(userException.getErrorInfo().getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidateException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorInfo errorInfo = ErrorInfo.valueOf(methodArgumentNotValidException.getFieldError().getDefaultMessage());
        apiResponse.setMessage(errorInfo.getMessage());
        apiResponse.setCode(errorInfo.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
