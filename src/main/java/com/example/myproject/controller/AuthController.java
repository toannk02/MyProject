package com.example.myproject.controller;

import com.example.myproject.dto.HttpResponse;
import com.example.myproject.dto.request.LoginRequest;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public HttpResponse<?> login(@RequestBody LoginRequest request) {
        return HttpResponse.ok(userService.loginByUsernamePass(request));
    }

}
