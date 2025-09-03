package com.phuocngo.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreation {
  @Size(min = 3, message = "INVALID_USERNAME")
  String username;

  @Size(min = 8, message = "INVALID_PASSWORD")
  String password;

  String fullName;
  LocalDate dob;
}
