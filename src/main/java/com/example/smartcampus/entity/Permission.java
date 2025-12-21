package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 权限名称

    @Column(nullable = false, unique = true)
    private String code; // 权限代码，如: user:read, user:write

    private String description; // 权限描述
    private String module;      // 所属模块

    public Permission() {}

    public Permission(String name, String code, String description, String module) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.module = module;
    }
}