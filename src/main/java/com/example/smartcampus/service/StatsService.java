package com.example.smartcampus.service;

import com.example.smartcampus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatsService {

    private final UserRepository userRepository;

    public StatsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long adminCount = userRepository.countByUserType("ADMIN");
        long teacherCount = userRepository.countByUserType("TEACHER");
        long studentCount = userRepository.countByUserType("STUDENT");
        long activeUsers = userRepository.findByStatus(1).size();

        stats.put("totalUsers", totalUsers);
        stats.put("adminCount", adminCount);
        stats.put("teacherCount", teacherCount);
        stats.put("studentCount", studentCount);
        stats.put("activeUsers", activeUsers);
        stats.put("inactiveUsers", totalUsers - activeUsers);

        return stats;
    }
}