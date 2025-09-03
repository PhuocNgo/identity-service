package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.dto.response.UserResponse;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.enums.Role;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.mapper.UserMapper;
import com.phuocngo.identity_service.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
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

  public UserResponse createUser(UserCreation userCreation) {
    if (userRepository.existsUserByUsername(userCreation.getUsername())) {
      throw new UserException(ErrorInfo.USER_EXISTED);
    }

    userCreation.setPassword(passwordEncoder.encode(userCreation.getPassword()));
    User user = userMapper.toUser(userCreation);
    var roles = new HashSet<String>();
    roles.add(Role.USER.getName());
    user.setRoles(roles);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public List<UserResponse> getUsers() {
    List<User> users = userRepository.findAll();
    List<UserResponse> userResponses = new ArrayList<>();

    SecurityContext securityContext = SecurityContextHolder.getContext();
    var authentication = securityContext.getAuthentication();
    log.info("username: {}", authentication.getName());

    var grantedAuthorities = authentication.getAuthorities();
    grantedAuthorities.forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

    for (User u : users) {
      userResponses.add(userMapper.toUserResponse(u));
    }

    return userResponses;
  }

  public UserResponse findUser(String id) {
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String id, UserUpdate userUpdate) {
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    user = userMapper.updateUser(user, userUpdate);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public String deleteUser(String id) {
    userRepository.deleteById(id);
    return "User has been deleted.";
  }
}
