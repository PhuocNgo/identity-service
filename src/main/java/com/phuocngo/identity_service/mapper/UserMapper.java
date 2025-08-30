package com.phuocngo.identity_service.mapper;

import com.phuocngo.identity_service.dto.payload.UserPayload;
import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.dto.response.UserResponse;
import com.phuocngo.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreation userCreation);
    UserResponse toUserResponse(User user);
    User updateUser(@MappingTarget User user, UserUpdate userUpdate);
    UserPayload toUserPayload(User user);
}
