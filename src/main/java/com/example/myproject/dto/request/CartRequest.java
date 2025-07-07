package com.example.myproject.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CartRequest {
    private Integer userId;
    private List<ProductQuantity> items;

    @Data
    public static class ProductQuantity {
        private Integer quantity;
        private Integer productId;
    }
}
