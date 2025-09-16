package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.dto.response.UserResponse;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.enums.Role;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.mapper.UserMapper;
import com.phuocngo.identity_service.repository.RoleRepository;
import com.phuocngo.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;

  public UserResponse createUser(UserCreation userCreation) {
    if (userRepository.existsUserByUsername(userCreation.getUsername())) {
      throw new UserException(ErrorInfo.USER_EXISTED);
    }

    userCreation.setPassword(passwordEncoder.encode(userCreation.getPassword()));
    User user = userMapper.toUser(userCreation);
    Set<String> roles = new HashSet<>();
    roles.add(Role.USER.getName());

    var rolesExisted = roleRepository.findAllById(roles);

    user.setRoles(new HashSet<>(rolesExisted));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<UserResponse> getUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(userMapper::toUserResponse).toList();
  }

  public UserResponse findUser(String id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserException(ErrorInfo.USER_NOT_EXISTED));
    return userMapper.toUserResponse(user);
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getMyInfo() {
    var account = SecurityContextHolder.getContext();
    String username = account.getAuthentication().getName();
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Not found"));
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String id, UserUpdate userUpdate) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserException(ErrorInfo.USER_NOT_EXISTED));

    String password = userUpdate.getPassword();
    if (password != null) {
      userUpdate.setPassword(passwordEncoder.encode(password));
    }

    userMapper.updateUser(user, userUpdate);

    var rolesReq = userUpdate.getRoles();
    if (rolesReq != null) {
      var roles = roleRepository.findAllById(rolesReq);
      user.setRoles(new HashSet<>(roles));
    }
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public String deleteUser(String id) {
    userRepository.deleteById(id);
    return "User has been deleted.";
  }
}
