package com.example.myproject.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_type")
    private String type;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;
}
