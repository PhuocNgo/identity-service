package com.phuocngo.identity_service.dto.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPayload {
    String username;
    String id;

    public String toString() {
        return "username:" + username + "id:" + id;
    }
}
