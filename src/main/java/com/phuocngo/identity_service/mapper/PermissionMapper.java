package com.phuocngo.identity_service.mapper;

import com.phuocngo.identity_service.dto.request.PermissionRequest;
import com.phuocngo.identity_service.dto.response.PermissionResponse;
import com.phuocngo.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  PermissionResponse toPermissionResponse(Permission permission);

  Permission toPermission(PermissionRequest permissionRequest);
}
