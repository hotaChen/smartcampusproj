package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Scholarship;
import com.example.smartcampus.repository.ScholarshipRepository;
import com.example.smartcampus.service.ScholarshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScholarshipServiceImpl implements ScholarshipService {

    private static final Logger logger = LoggerFactory.getLogger(ScholarshipServiceImpl.class);

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Override
    public Scholarship createScholarship(Scholarship scholarship) {
        logger.info("创建奖学金记录: 学生ID={}, 奖学金类型={}, 金额={}", 
                scholarship.getStudent().getId(), scholarship.getType(), scholarship.getAmount());
        
        Scholarship savedScholarship = scholarshipRepository.save(scholarship);
        logger.info("成功创建奖学金记录: ID={}", savedScholarship.getId());
        return savedScholarship;
    }

    @Override
    public Scholarship updateScholarship(Long id, Scholarship scholarship) {
        logger.info("更新奖学金记录: ID={}", id);
        
        Optional<Scholarship> existingScholarshipOpt = scholarshipRepository.findById(id);
        if (!existingScholarshipOpt.isPresent()) {
            logger.error("未找到奖学金记录: ID={}", id);
            throw new RuntimeException("未找到奖学金记录");
        }
        
        Scholarship existingScholarship = existingScholarshipOpt.get();
        
        // 更新字段
        if (scholarship.getAmount() != null) {
            existingScholarship.setAmount(scholarship.getAmount());
        }
        if (scholarship.getAwardDate() != null) {
            existingScholarship.setAwardDate(scholarship.getAwardDate());
        }
        if (scholarship.getDescription() != null) {
            existingScholarship.setDescription(scholarship.getDescription());
        }
        if (scholarship.getName() != null) {
            existingScholarship.setName(scholarship.getName());
        }
        if (scholarship.getReason() != null) {
            existingScholarship.setReason(scholarship.getReason());
        }
        if (scholarship.getSemester() != null) {
            existingScholarship.setSemester(scholarship.getSemester());
        }
        if (scholarship.getStatus() != null) {
            existingScholarship.setStatus(scholarship.getStatus());
        }
        if (scholarship.getType() != null) {
            existingScholarship.setType(scholarship.getType());
        }
        
        Scholarship updatedScholarship = scholarshipRepository.save(existingScholarship);
        logger.info("成功更新奖学金记录: ID={}", updatedScholarship.getId());
        return updatedScholarship;
    }

    @Override
    public void deleteScholarship(Long id) {
        logger.info("删除奖学金记录: ID={}", id);
        
        if (!scholarshipRepository.existsById(id)) {
            logger.error("未找到奖学金记录: ID={}", id);
            throw new RuntimeException("未找到奖学金记录");
        }
        
        scholarshipRepository.deleteById(id);
        logger.info("成功删除奖学金记录: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scholarship> getScholarshipById(Long id) {
        logger.debug("查询奖学金记录: ID={}", id);
        return scholarshipRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getAllScholarships() {
        logger.debug("查询所有奖学金记录");
        return scholarshipRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByStudent(Long studentId) {
        logger.debug("查询学生奖学金记录: 学生ID={}", studentId);
        return scholarshipRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByStudentStudentId(String studentId) {
        logger.debug("查询学生奖学金记录: 学号={}", studentId);
        return scholarshipRepository.findByStudentStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsBySemester(String semester) {
        logger.debug("查询学期奖学金记录: 学期={}", semester);
        return scholarshipRepository.findBySemester(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByStatus(Integer status) {
        logger.debug("查询状态奖学金记录: 状态={}", status);
        return scholarshipRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByType(String type) {
        logger.debug("查询类型奖学金记录: 类型={}", type);
        return scholarshipRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByStudentAndSemester(Long studentId, String semester) {
        logger.debug("查询学生学期奖学金记录: 学生ID={}, 学期={}", studentId, semester);
        return scholarshipRepository.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByStudentStudentIdAndSemester(String studentId, String semester) {
        logger.debug("查询学生学期奖学金记录: 学号={}, 学期={}", studentId, semester);
        return scholarshipRepository.findByStudentStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByDepartmentAndSemester(String department, String semester) {
        logger.debug("查询院系学期奖学金记录: 院系={}, 学期={}", department, semester);
        return scholarshipRepository.findByDepartmentAndSemester(department, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByMajorAndSemester(String major, String semester) {
        logger.debug("查询专业学期奖学金记录: 专业={}, 学期={}", major, semester);
        return scholarshipRepository.findByMajorAndSemester(major, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsByGradeAndSemester(Integer grade, String semester) {
        logger.debug("查询年级学期奖学金记录: 年级={}, 学期={}", grade, semester);
        return scholarshipRepository.findByGradeAndSemester(grade, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public ScholarshipStatistics getScholarshipStatistics(String semester) {
        logger.debug("查询奖学金统计信息: 学期={}", semester);
        
        ScholarshipStatistics statistics = new ScholarshipStatistics();
        
        // 总数统计
        statistics.setTotalCount(scholarshipRepository.count());
        
        // 各状态数量统计
        statistics.setApprovedCount(scholarshipRepository.countBySemesterAndStatus(semester, 1)); // 已批准
        statistics.setPendingCount(scholarshipRepository.countBySemesterAndStatus(semester, 0)); // 待审核
        statistics.setRejectedCount(scholarshipRepository.countBySemesterAndStatus(semester, 2)); // 已拒绝
        
        // 金额统计
        BigDecimal totalAmount = scholarshipRepository.sumAmountBySemesterAndStatus(semester, null);
        statistics.setTotalAmount(totalAmount != null ? totalAmount.doubleValue() : 0.0);
        
        BigDecimal approvedAmount = scholarshipRepository.sumAmountBySemesterAndStatus(semester, 1);
        statistics.setApprovedAmount(approvedAmount != null ? approvedAmount.doubleValue() : 0.0);
        
        // 类型统计
        List<Object[]> typeStats = scholarshipRepository.getStatisticsByTypeAndSemester(semester);
        List<TypeStatistics> typeStatistics = new ArrayList<>();
        
        for (Object[] stat : typeStats) {
            TypeStatistics typeStat = new TypeStatistics();
            typeStat.setType((String) stat[0]);
            typeStat.setCount((Long) stat[1]);
            // 处理BigDecimal类型
            if (stat[2] instanceof BigDecimal) {
                typeStat.setAmount(((BigDecimal) stat[2]).doubleValue());
            } else {
                typeStat.setAmount((Double) stat[2]);
            }
            typeStatistics.add(typeStat);
        }
        
        statistics.setTypeStatistics(typeStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentScholarshipStatistics getDepartmentScholarshipStatistics(String department, String semester) {
        logger.debug("查询院系奖学金统计信息: 院系={}, 学期={}", department, semester);
        
        DepartmentScholarshipStatistics statistics = new DepartmentScholarshipStatistics();
        statistics.setDepartment(department);
        
        // 获取院系奖学金记录
        List<Scholarship> scholarships = scholarshipRepository.findByDepartmentAndSemester(department, semester);
        
        // 总数统计
        statistics.setTotalCount((long) scholarships.size());
        
        // 各状态数量统计
        statistics.setApprovedCount(scholarships.stream().filter(s -> s.getStatus() == 1).count());
        statistics.setPendingCount(scholarships.stream().filter(s -> s.getStatus() == 0).count());
        statistics.setRejectedCount(scholarships.stream().filter(s -> s.getStatus() == 2).count());
        
        // 金额统计
        Double totalAmount = scholarships.stream().mapToDouble(s -> s.getAmount().doubleValue()).sum();
        statistics.setTotalAmount(totalAmount);
        
        Double approvedAmount = scholarships.stream()
                .filter(s -> s.getStatus() == 1)
                .mapToDouble(s -> s.getAmount().doubleValue())
                .sum();
        statistics.setApprovedAmount(approvedAmount);
        
        return statistics;
    }
}