package com.example.myproject.controller;

import com.example.myproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("cart")
//@RequiredArgsConstructor
//public class CartController {
//    private final OrderService orderService;
//    @PostMapping("/checkout")
//    public String checkout(@RequestParam Long userId) {
//        orderService.checkout(userId);
//        return "Checkout thành công!";
//    }
//}
