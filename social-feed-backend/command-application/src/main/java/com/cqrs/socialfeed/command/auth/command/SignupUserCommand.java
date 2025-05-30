package com.cqrs.socialfeed.command.auth.command;

public class SignupUserCommand {
    private final String username;
    private final String email;
    private final String password;
    private final String bio;

    public SignupUserCommand(String username, String email, String password, String bio) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }
}
