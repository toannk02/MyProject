package com.example.myproject.controller;

import com.example.myproject.dto.HttpResponse;
import com.example.myproject.dto.request.CartRequest;
import com.example.myproject.dto.request.SearchUserRequest;
import com.example.myproject.service.CartService;
import com.example.myproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/checkout")
    public HttpResponse<?> checkout(@RequestParam Integer userId) {
        return HttpResponse.ok(cartService.checkout(userId));
    }

    @PostMapping("/adđ-to-cart")
    public HttpResponse<?> addTocard(@RequestParam  CartRequest request) {
        return HttpResponse.ok(cartService.addToCard(request.getUserId(), request.getItems()));
    }
}
