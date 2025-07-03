package com.example.myproject.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Data
public class SearchUserResponse {
    private String username;
    private Boolean active;
}
