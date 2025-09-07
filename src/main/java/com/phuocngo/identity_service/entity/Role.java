package com.phuocngo.identity_service.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
  @Id String name;
  String description;

  @ManyToMany Set<Permission> permissions;
}
