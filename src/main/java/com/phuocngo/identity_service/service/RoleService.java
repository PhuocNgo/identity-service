package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.RoleRequest;
import com.phuocngo.identity_service.dto.response.RoleResponse;
import com.phuocngo.identity_service.entity.Role;
import com.phuocngo.identity_service.mapper.RoleMapper;
import com.phuocngo.identity_service.repository.PermissionRepository;
import com.phuocngo.identity_service.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;
  PermissionRepository permissionRepository;

  public RoleResponse createRole(RoleRequest roleRequest) {
    Role role = roleMapper.toRole(roleRequest);
    var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
    role.setPermissions(new HashSet<>(permissions));
    return roleMapper.toRoleResponse(roleRepository.save(role));
  }
  
  public List<RoleResponse> getPermissions() {
    return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
  }

  public String deleteRole(String role) {
    roleRepository.deleteById(role);
    return "Deleted role with name " + role;
  }
}
