package com.example.smartcampus.repository;

import com.example.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByStudentId(String studentId);

    Optional<User> findByTeacherId(String teacherId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByStudentId(String studentId);

    boolean existsByTeacherId(String teacherId);

    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findByUserType(@Param("userType") String userType);

    @Query("SELECT u FROM User u WHERE u.role.id = :roleId")
    List<User> findByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT u FROM User u WHERE u.department = :department")
    List<User> findByDepartment(@Param("department") String department);

    @Query("SELECT u FROM User u WHERE u.major = :major")
    List<User> findByMajor(@Param("major") String major);

    @Query("SELECT u FROM User u WHERE u.className = :className")
    List<User> findByClassName(@Param("className") String className);

    @Query("SELECT u FROM User u WHERE u.grade = :grade")
    List<User> findByGrade(@Param("grade") Integer grade);

    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") Integer status);

    @Query("SELECT u FROM User u WHERE u.realName LIKE %:name%")
    List<User> findByRealNameContaining(@Param("name") String name);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType")
    Long countByUserType(@Param("userType") String userType);

    @Query("SELECT u FROM User u WHERE u.createTime >= :startDate AND u.createTime <= :endDate")
    List<User> findByCreateTimeBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                       @Param("endDate") java.time.LocalDateTime endDate);
}