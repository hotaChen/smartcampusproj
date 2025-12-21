package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    List<Permission> findByModule(String module);

    @Query("SELECT p FROM Permission p WHERE p.name LIKE %:name%")
    List<Permission> findByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM Permission p WHERE p.code LIKE %:code%")
    List<Permission> findByCodeContaining(@Param("code") String code);

    boolean existsByCode(String code);
}