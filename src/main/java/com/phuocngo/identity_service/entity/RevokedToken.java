package com.phuocngo.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevokedToken {
  @Id
  String tokenId;
  Date expirationTime;
}
