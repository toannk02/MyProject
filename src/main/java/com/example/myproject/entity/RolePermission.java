package com.example.myproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permission")
public class RolePermission {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
