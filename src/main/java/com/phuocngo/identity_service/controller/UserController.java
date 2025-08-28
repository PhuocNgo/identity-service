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
import org.springframework.beans.factory.annotation.Autowired;
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
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(SuccessInfo.CREATED_USER.getCode());
        apiResponse.setMessage(SuccessInfo.CREATED_USER.getMessage());
        apiResponse.setResult(userService.createUser(userCreation));
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(SuccessInfo.GET_ALL_USERS.getCode());
        apiResponse.setMessage(SuccessInfo.GET_ALL_USERS.getMessage());
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.findUser(userId));
        apiResponse.setMessage(SuccessInfo.GET_USER_BY_ID.getMessage());
        apiResponse.setCode(SuccessInfo.GET_USER_BY_ID.getCode());
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdate userUpdate) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, userUpdate));
        apiResponse.setCode(SuccessInfo.UPDATE_USER_BY_ID.getCode());
        apiResponse.setMessage(SuccessInfo.UPDATE_USER_BY_ID.getMessage());
        return apiResponse;
    }

    @DeleteMapping("{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(userService.deleteUser(userId));
        apiResponse.setCode(SuccessInfo.DELETE_USER_BY_ID.getCode());
        return apiResponse;
    }
}
