package com.ecommerce.Project.security.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import java.util.List;

@Data
@Getter
@Setter
public class LoginResponse {
    private Long id;

    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public LoginResponse(Long id, String username, String jwtToken, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }
}
