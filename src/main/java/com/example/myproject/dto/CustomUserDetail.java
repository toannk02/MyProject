package com.example.myproject.dto;

import com.example.myproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetail implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return user.getUserRoles().stream()
               .flatMap(userRole -> userRole.getRole().getRolePermissions().stream())
               .map(rolePermission -> new SimpleGrantedAuthority(rolePermission.getPermission().getType()))
               .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
