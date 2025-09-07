package com.phuocngo.identity_service.controller;

import com.phuocngo.identity_service.dto.request.RoleRequest;
import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.dto.response.RoleResponse;
import com.phuocngo.identity_service.service.RoleService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
  RoleService roleService;

  @PostMapping
  public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<RoleResponse>builder()
                .code(2001)
                .result(roleService.createRole(request))
                .build());
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<RoleResponse>>> getPermissions() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            ApiResponse.<List<RoleResponse>>builder()
                .code(2000)
                .result(roleService.getPermissions())
                .build());
  }

  @DeleteMapping("/{role}")
  public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable String role) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            ApiResponse.<String>builder().code(2001).result(roleService.deleteRole(role)).build());
  }
}
