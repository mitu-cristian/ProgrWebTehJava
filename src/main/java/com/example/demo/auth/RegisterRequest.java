package com.example.demo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "The firstname should contain only letters.")
    private String firstname;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "The lastname should contain only letters.")
    private String lastname;
    @Email(message = "The email address is invalid")
    private String email;
    @NotNull(message = "Password should not be empty.")
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
