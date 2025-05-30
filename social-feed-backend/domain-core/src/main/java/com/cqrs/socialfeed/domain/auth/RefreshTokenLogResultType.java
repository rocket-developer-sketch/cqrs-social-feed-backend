package com.cqrs.socialfeed.domain.auth;

public enum RefreshTokenLogResultType {
    SUCCESS,
    INVALID_TOKEN,
    INVALID_USER,
    NO_TOKEN,
    MISMATCH,
    UNKNOWN,
    LOGOUT_SUCCESS,
    ALREADY_LOGGED_OUT,
    EXPIRED
}
