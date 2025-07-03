package com.example.myproject.dto.reponse;

import com.example.myproject.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private User user;
    private String token;
    private String role;
}
