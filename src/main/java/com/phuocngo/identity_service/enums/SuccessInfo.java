package com.phuocngo.identity_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SuccessInfo {
    CREATED_USER(2001, "User has been created."),
    GET_ALL_USERS(2002, "All users has been queried."),
    GET_USER_BY_ID(2003, "User has been queried by id."),
    UPDATE_USER_BY_ID(2004, "User has been updated."),
    DELETE_USER_BY_ID(2005, "User has been deleted.")
    ;

    final int code;
    final String message;

    SuccessInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
