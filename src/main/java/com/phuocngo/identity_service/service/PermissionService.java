package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.PermissionRequest;
import com.phuocngo.identity_service.dto.response.PermissionResponse;
import com.phuocngo.identity_service.entity.Permission;
import com.phuocngo.identity_service.mapper.PermissionMapper;
import com.phuocngo.identity_service.repository.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse createPermission(PermissionRequest request) {
    Permission permission = permissionRepository.save(permissionMapper.toPermission(request));
    return permissionMapper.toPermissionResponse(permission);
  }

  public List<PermissionResponse> getPermissions() {
    return permissionRepository.findAll().stream()
        .map(permissionMapper::toPermissionResponse)
        .toList();
  }

  public String deletePermission(String name) {
    permissionRepository.deleteById(name);
    return "Deleted permission";
  }
}
