package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_type")
    private String type;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RolePermission> rolePermissions;
}
