package com.example.smartcampus.repository;

import com.example.smartcampus.entity.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    // 根据模块查询错误日志
    Page<ErrorLog> findByModule(String module, Pageable pageable);

    // 根据错误级别查询
    Page<ErrorLog> findByLevel(String level, Pageable pageable);

    // 根据错误类型查询
    Page<ErrorLog> findByErrorType(String errorType, Pageable pageable);

    // 根据时间范围查询
    Page<ErrorLog> findByErrorTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // 根据用户名查询
    Page<ErrorLog> findByUsername(String username, Pageable pageable);

    // 根据类名查询
    Page<ErrorLog> findByClassNameContaining(String className, Pageable pageable);

    // 查找指定时间之前的日志（用于清理）
    List<ErrorLog> findByErrorTimeBefore(LocalDateTime beforeTime);

    // 复杂查询：多条件组合查询
    @Query("SELECT e FROM ErrorLog e WHERE " +
            "(:module IS NULL OR e.module = :module) AND " +
            "(:className IS NULL OR e.className LIKE %:className%) AND " +
            "(:errorType IS NULL OR e.errorType = :errorType) AND " +
            "(:level IS NULL OR e.level = :level) AND " +
            "(:startTime IS NULL OR e.errorTime >= :startTime) AND " +
            "(:endTime IS NULL OR e.errorTime <= :endTime) AND " +
            "(:username IS NULL OR e.username = :username) " +
            "ORDER BY e.errorTime DESC")
    Page<ErrorLog> findByConditions(@Param("module") String module,
                                    @Param("className") String className,
                                    @Param("errorType") String errorType,
                                    @Param("level") String level,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("username") String username,
                                    Pageable pageable);

    // 统计各模块的错误数量
    @Query("SELECT e.module, COUNT(e) FROM ErrorLog e GROUP BY e.module")
    List<Object[]> countByModule();

    // 统计各错误级别的数量
    @Query("SELECT e.level, COUNT(e) FROM ErrorLog e GROUP BY e.level")
    List<Object[]> countByLevel();

    // 统计最近N天的错误趋势
    @Query("SELECT CAST(e.errorTime AS date), COUNT(e) FROM ErrorLog e " +
            "WHERE e.errorTime >= :startDate GROUP BY CAST(e.errorTime AS date) " +
            "ORDER BY CAST(e.errorTime AS date) DESC")
    List<Object[]> countByDate(@Param("startDate") LocalDateTime startDate);

    // 获取最常见的错误类型
    @Query("SELECT e.errorType, COUNT(e) FROM ErrorLog e GROUP BY e.errorType ORDER BY COUNT(e) DESC")
    List<Object[]> findTopErrorTypes(Pageable pageable);

    // 检查是否存在未处理的严重错误
    @Query("SELECT COUNT(e) > 0 FROM ErrorLog e WHERE e.level = 'ERROR' AND e.errorTime >= :sinceTime")
    boolean hasRecentErrors(@Param("sinceTime") LocalDateTime sinceTime);

    // 统计总错误数
    long count();
}