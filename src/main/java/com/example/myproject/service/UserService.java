package com.example.myproject.service;

import com.example.myproject.dto.CustomUserDetail;
import com.example.myproject.dto.reponse.LoginResponse;
import com.example.myproject.entity.User;
import com.example.myproject.exception.RecordNotFoundException;
import com.example.myproject.exception.UnauthorizedException;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.dto.request.LoginRequest;
import com.example.myproject.security.jwt.JwtUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetail(user);
    }

    public LoginResponse loginByUsernamePass(LoginRequest request) {
        Optional<User> foundUser = userRepository.findByUsername(request.getUsername());
        if (foundUser.isEmpty()) {
            throw new RecordNotFoundException("user");
        }
        if (!foundUser.get().getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("password is incorrect!");
        }

        if (!foundUser.get().getActive()){
            throw new UnauthorizedException("your account is inactive!");
        }

        LoginResponse response = LoginResponse.builder()
                .token(jwtUtil.generateToken(loadUserByUsername(foundUser.get().getUsername())))
                .build();

        return response;
    }


}
