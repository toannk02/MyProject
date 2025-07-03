package com.example.myproject.dto.request;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private Integer id;
    private String username;
    private Integer roleId;
    private Integer permissionId;
    private Boolean active;
}
