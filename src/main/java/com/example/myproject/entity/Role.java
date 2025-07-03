package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RolePermission> rolePermissions;
}
