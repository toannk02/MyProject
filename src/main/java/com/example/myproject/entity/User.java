package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String username;
    private String password;
    private Boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserRole> userRoles;
}
