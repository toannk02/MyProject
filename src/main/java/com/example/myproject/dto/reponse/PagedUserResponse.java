package com.example.myproject.dto.reponse;

import lombok.Data;

import java.util.List;
@Data
public class PagedUserResponse {
    private List<SearchUserResponse> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
