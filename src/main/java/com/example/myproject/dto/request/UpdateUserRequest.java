package com.example.myproject.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    private Long id;

    private String username;

    private Boolean active;

    private List<Integer> roleIds;
}
