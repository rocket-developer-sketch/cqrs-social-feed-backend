package com.cqrs.socialfeed.api.auth;

public record LoginRequest(String username, String password) {}