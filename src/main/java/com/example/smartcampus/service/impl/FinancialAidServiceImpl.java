package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.FinancialAid;
import com.example.smartcampus.repository.FinancialAidRepository;
import com.example.smartcampus.service.FinancialAidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FinancialAidServiceImpl implements FinancialAidService {

    private static final Logger logger = LoggerFactory.getLogger(FinancialAidServiceImpl.class);

    @Autowired
    private FinancialAidRepository financialAidRepository;

    @Override
    public FinancialAid createFinancialAid(FinancialAid financialAid) {
        logger.info("创建助学金记录: 学生ID={}, 类型={}, 金额={}", 
                financialAid.getStudent().getId(), financialAid.getType(), financialAid.getAmount());
        
        FinancialAid savedFinancialAid = financialAidRepository.save(financialAid);
        logger.info("成功创建助学金记录: ID={}", savedFinancialAid.getId());
        
        return savedFinancialAid;
    }

    @Override
    public FinancialAid updateFinancialAid(Long id, FinancialAid financialAid) {
        logger.info("更新助学金记录: ID={}", id);
        
        Optional<FinancialAid> existingFinancialAidOpt = financialAidRepository.findById(id);
        if (!existingFinancialAidOpt.isPresent()) {
            logger.error("未找到助学金记录: ID={}", id);
            throw new RuntimeException("未找到助学金记录: ID=" + id);
        }
        
        FinancialAid existingFinancialAid = existingFinancialAidOpt.get();
        
        // 更新字段
        if (financialAid.getAmount() != null) {
            existingFinancialAid.setAmount(financialAid.getAmount());
        }
        if (financialAid.getName() != null) {
            existingFinancialAid.setName(financialAid.getName());
        }
        if (financialAid.getType() != null) {
            existingFinancialAid.setType(financialAid.getType());
        }
        if (financialAid.getSemester() != null) {
            existingFinancialAid.setSemester(financialAid.getSemester());
        }
        if (financialAid.getAwardDate() != null) {
            existingFinancialAid.setAwardDate(financialAid.getAwardDate());
        }
        if (financialAid.getDescription() != null) {
            existingFinancialAid.setDescription(financialAid.getDescription());
        }
        if (financialAid.getReason() != null) {
            existingFinancialAid.setReason(financialAid.getReason());
        }
        if (financialAid.getStatus() != null) {
            existingFinancialAid.setStatus(financialAid.getStatus());
        }
        
        FinancialAid updatedFinancialAid = financialAidRepository.save(existingFinancialAid);
        logger.info("成功更新助学金记录: ID={}", updatedFinancialAid.getId());
        
        return updatedFinancialAid;
    }

    @Override
    public void deleteFinancialAid(Long id) {
        logger.info("删除助学金记录: ID={}", id);
        
        if (!financialAidRepository.existsById(id)) {
            logger.error("未找到助学金记录: ID={}", id);
            throw new RuntimeException("未找到助学金记录: ID=" + id);
        }
        
        financialAidRepository.deleteById(id);
        logger.info("成功删除助学金记录: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinancialAid> getFinancialAidById(Long id) {
        logger.debug("查询助学金记录: ID={}", id);
        return financialAidRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getAllFinancialAids() {
        logger.debug("查询所有助学金记录");
        return financialAidRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByStudent(Long studentId) {
        logger.debug("查询学生助学金记录: 学生ID={}", studentId);
        return financialAidRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsBySemester(String semester) {
        logger.debug("查询学期助学金记录: 学期={}", semester);
        return financialAidRepository.findBySemester(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByStatus(Integer status) {
        logger.debug("查询状态助学金记录: 状态={}", status);
        return financialAidRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByType(String type) {
        logger.debug("查询类型助学金记录: 类型={}", type);
        return financialAidRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByStudentAndSemester(Long studentId, String semester) {
        logger.debug("查询学生学期助学金记录: 学生ID={}, 学期={}", studentId, semester);
        return financialAidRepository.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByDepartmentAndSemester(String department, String semester) {
        logger.debug("查询院系学期助学金记录: 院系={}, 学期={}", department, semester);
        return financialAidRepository.findByDepartmentAndSemester(department, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByMajorAndSemester(String major, String semester) {
        logger.debug("查询专业学期助学金记录: 专业={}, 学期={}", major, semester);
        return financialAidRepository.findByMajorAndSemester(major, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAid> getFinancialAidsByGradeAndSemester(Integer grade, String semester) {
        logger.debug("查询年级学期助学金记录: 年级={}, 学期={}", grade, semester);
        return financialAidRepository.findByGradeAndSemester(grade, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialAidStatistics getFinancialAidStatistics(String semester) {
        logger.debug("查询助学金统计信息: 学期={}", semester);
        
        FinancialAidStatistics statistics = new FinancialAidStatistics();
        
        // 总数统计
        Long totalCount = financialAidRepository.countBySemesterAndStatus(semester, null);
        statistics.setTotalCount(totalCount != null ? totalCount : 0L);
        
        // 按状态统计
        Long approvedCount = financialAidRepository.countBySemesterAndStatus(semester, 1); // 已批准
        Long pendingCount = financialAidRepository.countBySemesterAndStatus(semester, 0); // 待审核
        Long rejectedCount = financialAidRepository.countBySemesterAndStatus(semester, 2); // 已拒绝
        
        statistics.setApprovedCount(approvedCount != null ? approvedCount : 0L);
        statistics.setPendingCount(pendingCount != null ? pendingCount : 0L);
        statistics.setRejectedCount(rejectedCount != null ? rejectedCount : 0L);
        
        // 金额统计
        BigDecimal totalAmount = financialAidRepository.sumAmountBySemesterAndStatus(semester, null);
        BigDecimal approvedAmount = financialAidRepository.sumAmountBySemesterAndStatus(semester, 1);
        
        statistics.setTotalAmount(totalAmount != null ? totalAmount.doubleValue() : 0.0);
        statistics.setApprovedAmount(approvedAmount != null ? approvedAmount.doubleValue() : 0.0);
        
        // 按类型统计
        List<Object[]> typeStats = financialAidRepository.getStatisticsByTypeAndSemester(semester);
        List<TypeStatistics> typeStatistics = typeStats.stream()
                .map(this::convertToTypeStatistics)
                .collect(Collectors.toList());
        statistics.setTypeStatistics(typeStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentFinancialAidStatistics getDepartmentFinancialAidStatistics(String department, String semester) {
        logger.debug("查询院系助学金统计信息: 院系={}, 学期={}", department, semester);
        
        DepartmentFinancialAidStatistics statistics = new DepartmentFinancialAidStatistics();
        statistics.setDepartment(department);
        
        // 获取院系的所有助学金记录
        List<FinancialAid> departmentAids = financialAidRepository.findByDepartmentAndSemester(department, semester);
        
        // 总数统计
        statistics.setTotalCount((long) departmentAids.size());
        
        // 按状态统计
        long approvedCount = departmentAids.stream().filter(aid -> aid.getStatus() == 1).count();
        long pendingCount = departmentAids.stream().filter(aid -> aid.getStatus() == 0).count();
        long rejectedCount = departmentAids.stream().filter(aid -> aid.getStatus() == 2).count();
        
        statistics.setApprovedCount(approvedCount);
        statistics.setPendingCount(pendingCount);
        statistics.setRejectedCount(rejectedCount);
        
        // 金额统计
        double totalAmount = departmentAids.stream()
                .mapToDouble(aid -> aid.getAmount().doubleValue())
                .sum();
        double approvedAmount = departmentAids.stream()
                .filter(aid -> aid.getStatus() == 1)
                .mapToDouble(aid -> aid.getAmount().doubleValue())
                .sum();
        
        statistics.setTotalAmount(totalAmount);
        statistics.setApprovedAmount(approvedAmount);
        
        return statistics;
    }
    
    /**
     * 将Object[]数组转换为TypeStatistics对象
     */
    private TypeStatistics convertToTypeStatistics(Object[] obj) {
        TypeStatistics typeStatistics = new TypeStatistics();
        typeStatistics.setType((String) obj[0]);
        typeStatistics.setCount((Long) obj[1]);
        // 处理BigDecimal类型
        if (obj[2] instanceof BigDecimal) {
            typeStatistics.setAmount(((BigDecimal) obj[2]).doubleValue());
        } else {
            typeStatistics.setAmount((Double) obj[2]);
        }
        return typeStatistics;
    }
}