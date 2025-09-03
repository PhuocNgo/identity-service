package com.phuocngo.identity_service.repository;

import com.phuocngo.identity_service.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  boolean existsUserByUsername(String username);

  Optional<User> findByUsername(String username);
}
