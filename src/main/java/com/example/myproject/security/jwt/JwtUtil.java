package com.example.myproject.security.jwt;

import com.example.myproject.exception.RecordNotFoundException;
import com.example.myproject.repository.RoleRepository;
import com.example.myproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final String secret = "secret-key-demo123456789123456789";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        var s = userDetails.getUsername();
        var foundUser = userRepository.findByUsername(userDetails.getUsername());
        if (foundUser.isEmpty()) {
            throw new RecordNotFoundException("User");
        }

        List<String> roles = foundUser.get().getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();

        // Lấy danh sách permission từ user -> role
        List<String> permissions = foundUser.get().getUserRoles().stream()
                .flatMap(userRole -> userRole.getRole().getRolePermissions().stream())
                        .map(rolePermission -> rolePermission.getPermission().getType())
                                .distinct().toList();
        // thêm vào đây nữa
        claims.put("roles", roles);
        claims.put("permissions", permissions);
        claims.put("id", foundUser.get().getId());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // Check token đã hết hạn chưa và đúng với username không
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Map permission thành Authority
    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        List<String> permissions = claims.get("permissions", List.class);
        if (permissions == null) {
            return Collections.emptyList();
        }

        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
