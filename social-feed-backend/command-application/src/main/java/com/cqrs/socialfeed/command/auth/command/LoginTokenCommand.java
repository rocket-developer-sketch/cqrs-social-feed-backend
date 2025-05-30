package com.cqrs.socialfeed.command.auth.command;

public class LoginTokenCommand {
    private final String username;
    private final String password;

    public LoginTokenCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
