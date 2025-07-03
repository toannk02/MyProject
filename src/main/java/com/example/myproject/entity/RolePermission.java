package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role_permission")
@Data
public class RolePermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
