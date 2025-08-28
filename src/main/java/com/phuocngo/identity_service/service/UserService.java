package com.phuocngo.identity_service.service;

import com.phuocngo.identity_service.dto.request.UserCreation;
import com.phuocngo.identity_service.dto.request.UserUpdate;
import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.ErrorInfo;
import com.phuocngo.identity_service.exception.UserException;
import com.phuocngo.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User createUser(UserCreation userCreation) {
        User user = new User();

        if (userRepository.existsUserByUsername(userCreation.getUsername())) {
            throw new UserException(ErrorInfo.USER_EXISTED);
        }

        user.setDob(userCreation.getDob());
        user.setFullName(userCreation.getFullName());
        user.setPassword(userCreation.getPassword());
        user.setUsername(userCreation.getUsername());

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findUser(String id) {
        return  userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public User updateUser(String id, UserUpdate userUpdate) {
        User user = findUser(id);
        user.setDob(userUpdate.getDob());
        user.setFullName(userUpdate.getFullName());
        user.setPassword(userUpdate.getPassword());

        return userRepository.save(user);
    }

    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "User has been deleted";
    }
}
