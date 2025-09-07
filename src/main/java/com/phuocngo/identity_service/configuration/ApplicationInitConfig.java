package com.phuocngo.identity_service.configuration;

import com.phuocngo.identity_service.entity.User;
import com.phuocngo.identity_service.enums.Role;
import com.phuocngo.identity_service.repository.RoleRepository;
import com.phuocngo.identity_service.repository.UserRepository;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class ApplicationInitConfig {

  @Bean
  ApplicationRunner applicationRunner(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository) {

    return args -> {
      if (userRepository.findByUsername("admin").isPresent()) {
        return;
      }

      var roles = new HashSet<String>();
      roles.add(Role.ADMIN.getName());

      var rolesExisted = roleRepository.findAllById(roles);

      String password = passwordEncoder.encode("admin");

      User u =
          User.builder()
              .roles(new HashSet<>(rolesExisted))
              .username("admin")
              .password(password)
              .build();

      userRepository.save(u);

      log.warn("Your admin account default password is 'admin' please change it.");
    };
  }
}
