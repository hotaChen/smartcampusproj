package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.PaymentRecord;
import com.example.smartcampus.repository.PaymentRecordRepository;
import com.example.smartcampus.service.PaymentRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRecordServiceImpl.class);

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Override
    public PaymentRecord createPaymentRecord(PaymentRecord paymentRecord) {
        logger.info("创建缴费记录: 学生ID={}, 缴费类型={}, 金额={}", 
                paymentRecord.getStudent().getId(), paymentRecord.getPaymentType(), paymentRecord.getAmount());
        
        PaymentRecord savedPaymentRecord = paymentRecordRepository.save(paymentRecord);
        logger.info("成功创建缴费记录: ID={}", savedPaymentRecord.getId());
        
        return savedPaymentRecord;
    }

    @Override
    public PaymentRecord updatePaymentRecord(Long id, PaymentRecord paymentRecord) {
        logger.info("更新缴费记录: ID={}", id);
        
        Optional<PaymentRecord> existingPaymentRecordOpt = paymentRecordRepository.findById(id);
        if (!existingPaymentRecordOpt.isPresent()) {
            logger.error("未找到缴费记录: ID={}", id);
            throw new RuntimeException("未找到缴费记录: ID=" + id);
        }
        
        PaymentRecord existingPaymentRecord = existingPaymentRecordOpt.get();
        
        // 更新字段
        if (paymentRecord.getAmount() != null) {
            existingPaymentRecord.setAmount(paymentRecord.getAmount());
        }
        if (paymentRecord.getPaymentType() != null) {
            existingPaymentRecord.setPaymentType(paymentRecord.getPaymentType());
        }
        if (paymentRecord.getPaymentMethod() != null) {
            existingPaymentRecord.setPaymentMethod(paymentRecord.getPaymentMethod());
        }
        if (paymentRecord.getPaymentDate() != null) {
            existingPaymentRecord.setPaymentDate(paymentRecord.getPaymentDate());
        }
        if (paymentRecord.getSemester() != null) {
            existingPaymentRecord.setSemester(paymentRecord.getSemester());
        }
        if (paymentRecord.getTransactionId() != null) {
            existingPaymentRecord.setTransactionId(paymentRecord.getTransactionId());
        }
        if (paymentRecord.getDescription() != null) {
            existingPaymentRecord.setDescription(paymentRecord.getDescription());
        }
        if (paymentRecord.getStatus() != null) {
            existingPaymentRecord.setStatus(paymentRecord.getStatus());
        }
        
        PaymentRecord updatedPaymentRecord = paymentRecordRepository.save(existingPaymentRecord);
        logger.info("成功更新缴费记录: ID={}", updatedPaymentRecord.getId());
        
        return updatedPaymentRecord;
    }

    @Override
    public void deletePaymentRecord(Long id) {
        logger.info("删除缴费记录: ID={}", id);
        
        if (!paymentRecordRepository.existsById(id)) {
            logger.error("未找到缴费记录: ID={}", id);
            throw new RuntimeException("未找到缴费记录: ID=" + id);
        }
        
        paymentRecordRepository.deleteById(id);
        logger.info("成功删除缴费记录: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentRecord> getPaymentRecordById(Long id) {
        logger.debug("查询缴费记录: ID={}", id);
        return paymentRecordRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getAllPaymentRecords() {
        logger.debug("查询所有缴费记录");
        return paymentRecordRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByStudent(Long studentId) {
        logger.debug("查询学生缴费记录: 学生ID={}", studentId);
        return paymentRecordRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByStudentStudentId(String studentId) {
        logger.debug("查询学生缴费记录: 学号={}", studentId);
        return paymentRecordRepository.findByStudentStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsBySemester(String semester) {
        logger.debug("查询学期缴费记录: 学期={}", semester);
        return paymentRecordRepository.findBySemester(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByStatus(Integer status) {
        logger.debug("查询状态缴费记录: 状态={}", status);
        return paymentRecordRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByPaymentType(String paymentType) {
        logger.debug("查询缴费类型记录: 缴费类型={}", paymentType);
        return paymentRecordRepository.findByPaymentType(paymentType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByPaymentMethod(String paymentMethod) {
        logger.debug("查询缴费方式记录: 缴费方式={}", paymentMethod);
        return paymentRecordRepository.findByPaymentMethod(paymentMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByStudentAndSemester(Long studentId, String semester) {
        logger.debug("查询学生学期缴费记录: 学生ID={}, 学期={}", studentId, semester);
        return paymentRecordRepository.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByStudentStudentIdAndSemester(String studentId, String semester) {
        logger.debug("查询学生学期缴费记录: 学号={}, 学期={}", studentId, semester);
        return paymentRecordRepository.findByStudentStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("查询日期范围缴费记录: 开始时间={}, 结束时间={}", startDate, endDate);
        return paymentRecordRepository.findByPaymentDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByDepartmentAndSemester(String department, String semester) {
        logger.debug("查询院系学期缴费记录: 院系={}, 学期={}", department, semester);
        return paymentRecordRepository.findByDepartmentAndSemester(department, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByMajorAndSemester(String major, String semester) {
        logger.debug("查询专业学期缴费记录: 专业={}, 学期={}", major, semester);
        return paymentRecordRepository.findByMajorAndSemester(major, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentRecord> getPaymentRecordsByGradeAndSemester(Integer grade, String semester) {
        logger.debug("查询年级学期缴费记录: 年级={}, 学期={}", grade, semester);
        return paymentRecordRepository.findByGradeAndSemester(grade, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentRecordStatistics getPaymentRecordStatistics(String semester) {
        logger.debug("查询缴费统计信息: 学期={}", semester);
        
        PaymentRecordStatistics statistics = new PaymentRecordStatistics();
        
        // 获取学期所有缴费记录
        List<PaymentRecord> semesterRecords = paymentRecordRepository.findBySemester(semester);
        
        // 总数统计
        statistics.setTotalCount((long) semesterRecords.size());
        
        // 按状态统计
        long successCount = semesterRecords.stream().filter(record -> record.getStatus() == 1).count();
        long failedCount = semesterRecords.stream().filter(record -> record.getStatus() == 2).count();
        long pendingCount = semesterRecords.stream().filter(record -> record.getStatus() == 0).count();
        
        statistics.setSuccessCount(successCount);
        statistics.setFailedCount(failedCount);
        statistics.setPendingCount(pendingCount);
        
        // 金额统计
        double totalAmount = semesterRecords.stream()
                .mapToDouble(record -> record.getAmount().doubleValue())
                .sum();
        double successAmount = semesterRecords.stream()
                .filter(record -> record.getStatus() == 1)
                .mapToDouble(record -> record.getAmount().doubleValue())
                .sum();
        
        statistics.setTotalAmount(totalAmount);
        statistics.setSuccessAmount(successAmount);
        
        // 按缴费类型统计
        List<PaymentTypeStatistics> paymentTypeStatistics = semesterRecords.stream()
                .collect(Collectors.groupingBy(PaymentRecord::getPaymentType))
                .entrySet().stream()
                .map(entry -> {
                    PaymentTypeStatistics typeStats = new PaymentTypeStatistics();
                    typeStats.setPaymentType(entry.getKey());
                    typeStats.setCount((long) entry.getValue().size());
                    typeStats.setAmount(entry.getValue().stream()
                            .mapToDouble(record -> record.getAmount().doubleValue())
                            .sum());
                    return typeStats;
                })
                .collect(Collectors.toList());
        statistics.setPaymentTypeStatistics(paymentTypeStatistics);
        
        // 按缴费方式统计
        List<PaymentMethodStatistics> paymentMethodStatistics = semesterRecords.stream()
                .collect(Collectors.groupingBy(PaymentRecord::getPaymentMethod))
                .entrySet().stream()
                .map(entry -> {
                    PaymentMethodStatistics methodStats = new PaymentMethodStatistics();
                    methodStats.setPaymentMethod(entry.getKey());
                    methodStats.setCount((long) entry.getValue().size());
                    methodStats.setAmount(entry.getValue().stream()
                            .mapToDouble(record -> record.getAmount().doubleValue())
                            .sum());
                    return methodStats;
                })
                .collect(Collectors.toList());
        statistics.setPaymentMethodStatistics(paymentMethodStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentPaymentRecordStatistics getDepartmentPaymentRecordStatistics(String department, String semester) {
        logger.debug("查询院系缴费统计信息: 院系={}, 学期={}", department, semester);
        
        DepartmentPaymentRecordStatistics statistics = new DepartmentPaymentRecordStatistics();
        statistics.setDepartment(department);
        
        // 获取院系的所有缴费记录
        List<PaymentRecord> departmentRecords = paymentRecordRepository.findByDepartmentAndSemester(department, semester);
        
        // 总数统计
        statistics.setTotalCount((long) departmentRecords.size());
        
        // 按状态统计
        long successCount = departmentRecords.stream().filter(record -> record.getStatus() == 1).count();
        long failedCount = departmentRecords.stream().filter(record -> record.getStatus() == 2).count();
        long pendingCount = departmentRecords.stream().filter(record -> record.getStatus() == 0).count();
        
        statistics.setSuccessCount(successCount);
        statistics.setFailedCount(failedCount);
        statistics.setPendingCount(pendingCount);
        
        // 金额统计
        double totalAmount = departmentRecords.stream()
                .mapToDouble(record -> record.getAmount().doubleValue())
                .sum();
        double successAmount = departmentRecords.stream()
                .filter(record -> record.getStatus() == 1)
                .mapToDouble(record -> record.getAmount().doubleValue())
                .sum();
        
        statistics.setTotalAmount(totalAmount);
        statistics.setSuccessAmount(successAmount);
        
        return statistics;
    }
}