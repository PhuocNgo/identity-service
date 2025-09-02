package com.phuocngo.identity_service.controller;

import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.dto.response.UserResponse;
import com.phuocngo.identity_service.enums.SuccessInfo;
import com.phuocngo.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreation userCreation) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .code(SuccessInfo.CREATED_USER.getCode())
                        .result(userService.createUser(userCreation))
                        .message(SuccessInfo.CREATED_USER.getMessage())
                        .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {

        return ResponseEntity
                .ok()
                .body(ApiResponse.<List<UserResponse>>builder()
                        .code(SuccessInfo.GET_ALL_USERS.getCode())
                        .message(SuccessInfo.GET_ALL_USERS.getMessage())
                        .result(userService.getUsers())
                        .build()
                );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {

        return ResponseEntity
                .ok()
                .body(ApiResponse.<UserResponse>builder()
                        .code(SuccessInfo.GET_USER_BY_ID.getCode())
                        .message(SuccessInfo.GET_USER_BY_ID.getMessage())
                        .result(userService.findUser(userId))
                        .build()
                );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId, @RequestBody UserUpdate userUpdate) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ApiResponse.<UserResponse>builder()
                        .code(SuccessInfo.UPDATE_USER_BY_ID.getCode())
                        .result(userService.updateUser(userId, userUpdate))
                        .message(SuccessInfo.UPDATE_USER_BY_ID.getMessage())
                        .build()
                );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .result(userService.deleteUser(userId))
                        .message(SuccessInfo.DELETE_USER_BY_ID.getMessage())
                        .code(SuccessInfo.DELETE_USER_BY_ID.getCode())
                        .build()
                );
    }
}
