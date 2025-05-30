package com.cqrs.socialfeed.api.auth;

public record SignupRequest (
    String username,
    String email,
    String password,
    String bio
){}
