package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Tuition;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.TuitionRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.TuitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TuitionServiceImpl implements TuitionService {

    private static final Logger logger = LoggerFactory.getLogger(TuitionServiceImpl.class);

    private final TuitionRepository tuitionRepository;
    private final UserRepository userRepository;

    public TuitionServiceImpl(TuitionRepository tuitionRepository, UserRepository userRepository) {
        this.tuitionRepository = tuitionRepository;
        this.userRepository = userRepository;
        logger.info("TuitionServiceImpl 初始化完成");
    }

    @Override
    public Tuition createTuition(Tuition tuition) {
        logger.info("=== TuitionServiceImpl.createTuition() 开始 ===");
        logger.info("创建学费记录: 学生ID={}, 学期={}, 金额={}", 
                tuition.getStudent().getId(), tuition.getSemester(), tuition.getAmount());

        // 检查是否已存在该学生该学期的学费记录
        Optional<Tuition> existingTuition = tuitionRepository.findByStudentIdAndSemester(
                tuition.getStudent().getId(), tuition.getSemester());
        
        if (existingTuition.isPresent()) {
            logger.warn("该学生{}的学期{}已存在学费记录", tuition.getStudent().getId(), tuition.getSemester());
            throw new RuntimeException("该学生该学期的学费记录已存在");
        }

        // 初始化缴费状态
        tuition.updatePaymentStatus();
        
        Tuition savedTuition = tuitionRepository.save(tuition);
        logger.info("✅ 成功创建学费记录: ID={}", savedTuition.getId());
        return savedTuition;
    }

    @Override
    public Tuition updateTuition(Long id, Tuition tuition) {
        logger.info("=== TuitionServiceImpl.updateTuition() 开始 ===");
        logger.info("更新学费记录: ID={}", id);

        Optional<Tuition> tuitionOpt = tuitionRepository.findById(id);
        if (!tuitionOpt.isPresent()) {
            logger.warn("未找到学费记录: ID={}", id);
            throw new RuntimeException("学费记录不存在");
        }

        Tuition existingTuition = tuitionOpt.get();
        
        // 更新字段
        if (tuition.getAmount() != null) {
            existingTuition.setAmount(tuition.getAmount());
        }
        if (tuition.getSemester() != null) {
            existingTuition.setSemester(tuition.getSemester());
        }
        if (tuition.getDueDate() != null) {
            existingTuition.setDueDate(tuition.getDueDate());
        }
        if (tuition.getDescription() != null) {
            existingTuition.setDescription(tuition.getDescription());
        }
        
        // 更新缴费状态
        existingTuition.updatePaymentStatus();
        
        Tuition updatedTuition = tuitionRepository.save(existingTuition);
        logger.info("✅ 成功更新学费记录: ID={}", updatedTuition.getId());
        return updatedTuition;
    }

    @Override
    public void deleteTuition(Long id) {
        logger.info("=== TuitionServiceImpl.deleteTuition() 开始 ===");
        logger.info("删除学费记录: ID={}", id);

        if (!tuitionRepository.existsById(id)) {
            logger.warn("未找到学费记录: ID={}", id);
            throw new RuntimeException("学费记录不存在");
        }

        tuitionRepository.deleteById(id);
        logger.info("✅ 成功删除学费记录: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tuition> getTuitionById(Long id) {
        logger.info("=== TuitionServiceImpl.getTuitionById() 开始 ===");
        logger.info("查询学费记录: ID={}", id);

        return tuitionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuition> getAllTuitions() {
        logger.info("=== TuitionServiceImpl.getAllTuitions() 开始 ===");
        List<Tuition> tuitions = tuitionRepository.findAll();
        logger.info("查询到 {} 条学费记录", tuitions.size());
        return tuitions;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuition> getTuitionsByStudent(Long studentId) {
        logger.info("=== TuitionServiceImpl.getTuitionsByStudent() 开始 ===");
        logger.info("查询学生学费记录: 学生ID={}", studentId);
        
        return tuitionRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuition> getTuitionsBySemester(String semester) {
        logger.info("=== TuitionServiceImpl.getTuitionsBySemester() 开始 ===");
        logger.info("查询学期学费记录: 学期={}", semester);
        
        return tuitionRepository.findBySemester(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuition> getTuitionsByStatus(Integer status) {
        logger.info("=== TuitionServiceImpl.getTuitionsByStatus() 开始 ===");
        logger.info("查询状态学费记录: 状态={}", status);
        
        return tuitionRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tuition> getTuitionByStudentAndSemester(Long studentId, String semester) {
        logger.info("=== TuitionServiceImpl.getTuitionByStudentAndSemester() 开始 ===");
        logger.info("查询学生学期学费记录: 学生ID={}, 学期={}", studentId, semester);
        
        return tuitionRepository.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tuition> getOverdueTuitions() {
        logger.info("=== TuitionServiceImpl.getOverdueTuitions() 开始 ===");
        
        return tuitionRepository.findOverdueTuitions(LocalDateTime.now());
    }

    @Override
    public Tuition makePayment(Long tuitionId, BigDecimal paymentAmount) {
        logger.info("=== TuitionServiceImpl.makePayment() 开始 ===");
        logger.info("学费缴费: 学费ID={}, 缴费金额={}", tuitionId, paymentAmount);

        Optional<Tuition> tuitionOpt = tuitionRepository.findById(tuitionId);
        if (!tuitionOpt.isPresent()) {
            logger.warn("未找到学费记录: ID={}", tuitionId);
            throw new RuntimeException("学费记录不存在");
        }

        Tuition tuition = tuitionOpt.get();
        
        // 检查缴费金额是否有效
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("无效的缴费金额: {}", paymentAmount);
            throw new RuntimeException("缴费金额必须大于0");
        }
        
        // 检查缴费金额是否超过未缴金额
        if (paymentAmount.compareTo(tuition.getUnpaidAmount()) > 0) {
            logger.warn("缴费金额超过未缴金额: 缴费金额={}, 未缴金额={}", 
                    paymentAmount, tuition.getUnpaidAmount());
            throw new RuntimeException("缴费金额不能超过未缴金额");
        }
        
        // 添加缴费金额
        tuition.addPayment(paymentAmount);
        
        Tuition updatedTuition = tuitionRepository.save(tuition);
        logger.info("✅ 成功缴费: 学费ID={}, 缴费金额={}, 当前已缴金额={}", 
                updatedTuition.getId(), paymentAmount, updatedTuition.getPaidAmount());
        return updatedTuition;
    }

    @Override
    public List<Tuition> createTuitionsForStudents(List<User> students, BigDecimal amount, 
                                                 String semester, LocalDateTime dueDate) {
        logger.info("=== TuitionServiceImpl.createTuitionsForStudents() 开始 ===");
        logger.info("批量创建学费记录: 学生数量={}, 金额={}, 学期={}", 
                students.size(), amount, semester);

        List<Tuition> tuitions = students.stream()
                .map(student -> {
                    // 检查是否已存在该学生该学期的学费记录
                    Optional<Tuition> existingTuition = tuitionRepository.findByStudentIdAndSemester(
                            student.getId(), semester);
                    
                    if (existingTuition.isPresent()) {
                        logger.warn("学生{}的学期{}已存在学费记录，跳过创建", student.getId(), semester);
                        return null;
                    }
                    
                    Tuition tuition = new Tuition(student, amount, semester, dueDate);
                    tuition.updatePaymentStatus();
                    return tuition;
                })
                .filter(tuition -> tuition != null)
                .collect(Collectors.toList());
        
        List<Tuition> savedTuitions = tuitionRepository.saveAll(tuitions);
        logger.info("✅ 成功创建 {} 条学费记录", savedTuitions.size());
        return savedTuitions;
    }

    @Override
    @Transactional(readOnly = true)
    public TuitionStatistics getTuitionStatistics(String semester) {
        logger.info("=== TuitionServiceImpl.getTuitionStatistics() 开始 ===");
        logger.info("查询学费统计: 学期={}", semester);

        Double totalAmount = tuitionRepository.getTotalAmountBySemester(semester);
        Double totalPaidAmount = tuitionRepository.getTotalPaidAmountBySemester(semester);
        
        BigDecimal totalAmountBD = totalAmount != null ? BigDecimal.valueOf(totalAmount) : BigDecimal.ZERO;
        BigDecimal totalPaidAmountBD = totalPaidAmount != null ? BigDecimal.valueOf(totalPaidAmount) : BigDecimal.ZERO;
        
        Long totalCount = tuitionRepository.countByStatusAndSemester(null, semester);
        Long paidCount = tuitionRepository.countByStatusAndSemester(2, semester); // 已缴费
        Long unpaidCount = tuitionRepository.countByStatusAndSemester(0, semester); // 未缴费
        Long partiallyPaidCount = tuitionRepository.countByStatusAndSemester(1, semester); // 部分缴费
        
        TuitionStatistics statistics = new TuitionStatistics(
                semester, totalAmountBD, totalPaidAmountBD, 
                totalCount, paidCount, unpaidCount, partiallyPaidCount);
        
        logger.info("✅ 学费统计查询完成: 学期={}, 总金额={}, 已缴金额={}, 未缴金额={}", 
                semester, totalAmountBD, totalPaidAmountBD, totalAmountBD.subtract(totalPaidAmountBD));
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentTuitionStatistics getDepartmentTuitionStatistics(String department, String semester) {
        logger.info("=== TuitionServiceImpl.getDepartmentTuitionStatistics() 开始 ===");
        logger.info("查询院系学费统计: 院系={}, 学期={}", department, semester);

        List<Tuition> tuitions = tuitionRepository.findByDepartmentAndSemester(department, semester);
        
        BigDecimal totalAmount = tuitions.stream()
                .map(Tuition::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalPaidAmount = tuitions.stream()
                .map(Tuition::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Long totalCount = (long) tuitions.size();
        Long paidCount = tuitions.stream()
                .filter(t -> t.getStatus() == 2)
                .count();
        Long unpaidCount = tuitions.stream()
                .filter(t -> t.getStatus() == 0)
                .count();
        Long partiallyPaidCount = tuitions.stream()
                .filter(t -> t.getStatus() == 1)
                .count();
        
        DepartmentTuitionStatistics statistics = new DepartmentTuitionStatistics(
                department, semester, totalAmount, totalPaidAmount, 
                totalCount, paidCount, unpaidCount, partiallyPaidCount);
        
        logger.info("✅ 院系学费统计查询完成: 院系={}, 学期={}, 总金额={}, 已缴金额={}, 未缴金额={}", 
                department, semester, totalAmount, totalPaidAmount, totalAmount.subtract(totalPaidAmount));
        
        return statistics;
    }
}