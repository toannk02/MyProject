package com.example.myproject.service;

import com.example.myproject.dto.CustomUserDetail;
import com.example.myproject.dto.HttpResponse;
import com.example.myproject.dto.reponse.CreateUserResponse;
import com.example.myproject.dto.request.CreateUserRequest;
import com.example.myproject.dto.reponse.LoginResponse;
import com.example.myproject.dto.reponse.PagedUserResponse;
import com.example.myproject.dto.reponse.SearchUserResponse;
import com.example.myproject.dto.request.SearchUserRequest;
import com.example.myproject.dto.request.UpdateUserRequest;
import com.example.myproject.entity.Role;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.exception.NameAlreadyExistsException;
import com.example.myproject.exception.RecordNotFoundException;
import com.example.myproject.exception.UnauthorizedException;
import com.example.myproject.repository.RoleRepository;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.dto.request.LoginRequest;
import com.example.myproject.repository.UserRoleRepository;
import com.example.myproject.security.jwt.JwtUtil;
import com.example.myproject.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
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


    public Object search(Object objectRequest) {
        var request = (SearchUserRequest) objectRequest;
        request.validateInput();
        Pageable pageable;
        if(request.getOrderBy().equals("DESC")){
            pageable = PageRequest.of(request.getPageIndex() - 1, request.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        }else {
            pageable = PageRequest.of(request.getPageIndex() - 1, request.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
        }

        Page<User> users = userRepository.search(
                request.getKeyWord(),
                request.getUsername(),
                request.getActive(),
                pageable
        );

        List<SearchUserResponse> content = users.stream()
                .map(user -> {
                    SearchUserResponse dto = new SearchUserResponse();
                    dto.setUsername(user.getUsername());
                    dto.setActive(user.getActive());
                    return dto;
                })
                .toList();
        PagedUserResponse response = new PagedUserResponse();
        response.setContent(content);
        response.setCurrentPage(users.getNumber());
        response.setTotalPages(users.getTotalPages());
        response.setTotalElements(users.getTotalElements());

        return response;
    }

    @Transactional
    public Object create(CreateUserRequest request) {
        var foundUser = userRepository.findByUsername(request.getUsername());
        if (foundUser.isPresent()) {
            throw new NameAlreadyExistsException("user");
        }

        // Validate password
        var rawPassword = Constants.randomPassword(request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(rawPassword);
        user.setActive(request.getActive() != null ? request.getActive() : true);
        userRepository.save(user);

        var role = roleRepository.findById(Long.valueOf(request.getRoleId()))
                .orElseThrow(() -> new RecordNotFoundException("role"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        CreateUserResponse response = new CreateUserResponse();
        response.setUsername(request.getUsername());
        response.setActive(user.getActive());
        response.setRole(role.getName());

        return response;
    }

    public Object delete(Integer id) {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RecordNotFoundException("User"));
        user.setActive(false);
        userRepository.save(user);
        return HttpResponse.ok("Delete user success (soft delete)");
    }

    @Transactional
    public Object update(UpdateUserRequest request) {
        var foundUser = userRepository.findById(request.getId());
        if (foundUser.isEmpty()) {
            throw new RecordNotFoundException("user");
        }

        if(request.getUsername() != null && request.getUsername().equals(foundUser.get().getUsername())){
            throw new NameAlreadyExistsException("username");
        }
        foundUser.get().setUsername(request.getUsername());
        foundUser.get().setActive(request.getActive());

        // Cập nhật role nếu có
        this.deleteRoles(request.getRoleIds(), foundUser.get());

        userRepository.save(foundUser.get());
        return "Cập nhật thành công";
    }

    @Transactional
    public void deleteRoles(List<Integer> roleIds, User user) {
        userRoleRepository.deleteByUserId(user.getId());

        // Thêm lại role theo request
        for (Integer roleId : roleIds) {
            Role role = roleRepository.findById(Long.valueOf(roleId))
                    .orElseThrow(() -> new RecordNotFoundException("Role"));

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }
    }
}
