package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.dto.response.UserResponse;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.mapper.UserMapper;
import com.phuocngo.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreation userCreation) {
        if (userRepository.existsUserByUsername(userCreation.getUsername())) {
            throw new UserException(ErrorInfo.USER_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        userCreation.setPassword(passwordEncoder.encode(userCreation.getPassword()));
        User user = userMapper.toUser(userCreation);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
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
