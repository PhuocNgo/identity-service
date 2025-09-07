package com.phuocngo.identity_service.controller;

import com.phuocngo.identity_service.dto.request.PermissionRequest;
import com.phuocngo.identity_service.dto.response.ApiResponse;
import com.phuocngo.identity_service.dto.response.PermissionResponse;
import com.phuocngo.identity_service.service.PermissionService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
  PermissionService permissionService;

  @PostMapping
  public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
      @RequestBody PermissionRequest permissionRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<PermissionResponse>builder()
                .code(3001)
                .result(permissionService.createPermission(permissionRequest))
                .build());
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissions() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            ApiResponse.<List<PermissionResponse>>builder()
                .code(3000)
                .result(permissionService.getPermissions())
                .build());
  }

  @DeleteMapping("/{permission}")
  public ResponseEntity<ApiResponse<String>> deletePermission(@PathVariable String permission) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            ApiResponse.<String>builder()
                .code(3002)
                .result(permissionService.deletePermission(permission))
                .build());
  }
}
