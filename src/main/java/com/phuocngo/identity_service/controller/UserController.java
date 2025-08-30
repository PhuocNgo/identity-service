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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreation userCreation) {

        return ApiResponse.<UserResponse>builder()
                .code(SuccessInfo.CREATED_USER.getCode())
                .result(userService.createUser(userCreation))
                .message(SuccessInfo.CREATED_USER.getMessage())
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {

        return ApiResponse.<List<UserResponse>>builder()
                .code(SuccessInfo.GET_ALL_USERS.getCode())
                .message(SuccessInfo.GET_ALL_USERS.getMessage())
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId) {

        return ApiResponse.<UserResponse>builder()
                .code(SuccessInfo.GET_USER_BY_ID.getCode())
                .message(SuccessInfo.GET_USER_BY_ID.getMessage())
                .result(userService.findUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdate userUpdate) {

        return ApiResponse.<UserResponse>builder()
                .code(SuccessInfo.UPDATE_USER_BY_ID.getCode())
                .result(userService.updateUser(userId, userUpdate))
                .message(SuccessInfo.UPDATE_USER_BY_ID.getMessage())
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {

        return ApiResponse.<String>builder()
                .result(userService.deleteUser(userId))
                .message(SuccessInfo.DELETE_USER_BY_ID.getMessage())
                .code(SuccessInfo.DELETE_USER_BY_ID.getCode())
                .build();
    }
}
