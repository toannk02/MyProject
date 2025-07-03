package com.example.myproject.controller;

import com.example.myproject.dto.HttpResponse;
import com.example.myproject.dto.request.CreateUserRequest;
import com.example.myproject.dto.request.SearchUserRequest;
import com.example.myproject.dto.request.UpdateUserRequest;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping("view")
    public HttpResponse<?> getAll(@RequestBody SearchUserRequest request) {
        return HttpResponse.ok(userService.search(request));
    }

    @PostMapping("create")
    @PreAuthorize("hasAuthority('user:create')")
    public HttpResponse<?> create(@RequestBody CreateUserRequest request) {
        return HttpResponse.ok(userService.create(request));
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public HttpResponse<?> delete(@RequestParam Integer id) {
        return HttpResponse.ok(userService.delete(id));
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('user:update')")
    public HttpResponse<?> update(@RequestBody UpdateUserRequest request) {
        return HttpResponse.ok(userService.update(request));
    }
}
