package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r WHERE r.name LIKE %:name%")
    List<Role> findByNameContaining(@Param("name") String name);

    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.code = :permissionCode")
    List<Role> findByPermissionCode(@Param("permissionCode") String permissionCode);

    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE r.description LIKE %:description%")
    List<Role> findByDescriptionContaining(@Param("description") String description);
}