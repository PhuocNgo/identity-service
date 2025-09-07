package com.phuocngo.identity_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Role {
  ADMIN("ADMIN"),
  USER("USER");

  final String name;

  Role(String name) {
    this.name = name;
  }
}
