package com.example.myproject.dto.reponse;

import lombok.Data;

@Data
public class CreateUserResponse {
    private String username;
    private String role;
    private Boolean active;
}
