package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.MakeupExamApprovalRequest;
import com.example.smartcampus.dto.MakeupExamGradeRequest;
import com.example.smartcampus.dto.MakeupExamRequest;
import com.example.smartcampus.entity.Grade;
import com.example.smartcampus.entity.MakeupExam;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.GradeRepository;
import com.example.smartcampus.repository.MakeupExamRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.MakeupExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MakeupExamServiceImpl implements MakeupExamService {

    private final MakeupExamRepository makeupExamRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public MakeupExamServiceImpl(MakeupExamRepository makeupExamRepository,
                                 GradeRepository gradeRepository,
                                 UserRepository userRepository) {
        this.makeupExamRepository = makeupExamRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MakeupExam applyForMakeupExam(MakeupExamRequest makeupExamRequest, Long studentId) {
        // 获取学生信息
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        
        // 获取原始成绩信息
        Grade originalGrade = gradeRepository.findById(makeupExamRequest.getOriginalGradeId())
                .orElseThrow(() -> new RuntimeException("原始成绩不存在"));
        
        // 验证成绩是否属于该学生
        if (!originalGrade.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("该成绩不属于当前学生");
        }
        
        // 检查是否已经申请过补考
        Optional<MakeupExam> existingMakeupExam = makeupExamRepository.findByOriginalGradeId(makeupExamRequest.getOriginalGradeId());
        if (existingMakeupExam.isPresent()) {
            throw new RuntimeException("该成绩已申请过补考");
        }
        
        // 创建补考记录
        MakeupExam makeupExam = new MakeupExam();
        makeupExam.setCourseCode(makeupExamRequest.getCourseCode());
        makeupExam.setCourseName(makeupExamRequest.getCourseName());
        makeupExam.setExamDate(makeupExamRequest.getExamDate());
        makeupExam.setExamLocation(makeupExamRequest.getExamLocation());
        makeupExam.setSemester(makeupExamRequest.getSemester());
        makeupExam.setOriginalGrade(originalGrade.getGradeLevel());
        makeupExam.setOriginalGradeId(makeupExamRequest.getOriginalGradeId());
        makeupExam.setStudent(student);
        makeupExam.setTeacher(originalGrade.getTeacher());
        makeupExam.setApplyReason(makeupExamRequest.getApplyReason());
        makeupExam.setApplyTime(LocalDateTime.now());
        
        return makeupExamRepository.save(makeupExam);
    }

    @Override
    public MakeupExam approveMakeupExam(Long makeupExamId, MakeupExamApprovalRequest approvalRequest, Long teacherId) {
        MakeupExam makeupExam = makeupExamRepository.findById(makeupExamId)
                .orElseThrow(() -> new RuntimeException("补考记录不存在"));
        
        // 验证是否为该教师负责的补考
        if (!makeupExam.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("无权限审批此补考申请");
        }
        
        // 验证状态
        if (!"待审核".equals(makeupExam.getStatus())) {
            throw new RuntimeException("只能审批待审核状态的补考申请");
        }
        
        // 更新状态和审批信息
        makeupExam.setStatus(approvalRequest.getStatus());
        makeupExam.setApprovalRemark(approvalRequest.getApprovalRemark());
        makeupExam.setApprovalTime(LocalDateTime.now());
        makeupExam.setUpdateTime(LocalDateTime.now());
        
        return makeupExamRepository.save(makeupExam);
    }

    @Override
    public MakeupExam enterMakeupExamGrade(Long makeupExamId, MakeupExamGradeRequest gradeRequest, Long teacherId) {
        MakeupExam makeupExam = makeupExamRepository.findById(makeupExamId)
                .orElseThrow(() -> new RuntimeException("补考记录不存在"));
        
        // 验证是否为该教师负责的补考
        if (!makeupExam.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("无权限录入此补考成绩");
        }
        
        // 验证状态
        if (!"已批准".equals(makeupExam.getStatus())) {
            throw new RuntimeException("只能为已批准的补考录入成绩");
        }
        
        // 更新成绩信息
        makeupExam.setScore(gradeRequest.getScore());
        makeupExam.setGradeLevel(gradeRequest.getGradeLevel());
        makeupExam.setMakeupGrade(gradeRequest.getGradeLevel()); // 同时更新makeupGrade字段
        makeupExam.setComment(gradeRequest.getComment());
        makeupExam.setStatus("已完成");
        makeupExam.setUpdateTime(LocalDateTime.now());
        
        return makeupExamRepository.save(makeupExam);
    }

    @Override
    public Optional<MakeupExam> getMakeupExamById(Long makeupExamId) {
        return makeupExamRepository.findById(makeupExamId);
    }

    @Override
    public List<MakeupExam> getMakeupExamsByStudentId(Long studentId) {
        return makeupExamRepository.findByStudentId(studentId);
    }

    @Override
    public List<MakeupExam> getMakeupExamsByTeacherId(Long teacherId) {
        return makeupExamRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<MakeupExam> getStudentMakeupExamsBySemester(Long studentId, String semester) {
        return makeupExamRepository.findStudentMakeupExamsBySemester(studentId, semester);
    }

    @Override
    public List<MakeupExam> getTeacherCourseMakeupExamsBySemester(Long teacherId, String courseCode, String semester) {
        return makeupExamRepository.findTeacherCourseMakeupExamsBySemester(teacherId, courseCode, semester);
    }

    @Override
    public List<MakeupExam> getPendingMakeupExams() {
        return makeupExamRepository.findByStatus("待审核");
    }

    @Override
    public boolean deleteMakeupExam(Long makeupExamId, Long teacherId) {
        MakeupExam makeupExam = makeupExamRepository.findById(makeupExamId)
                .orElseThrow(() -> new RuntimeException("补考记录不存在"));
        
        // 验证是否为该教师负责的补考
        if (!makeupExam.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("无权限删除此补考记录");
        }
        
        // 只有待审核或已拒绝状态才能删除
        if (!"待审核".equals(makeupExam.getStatus()) && !"已拒绝".equals(makeupExam.getStatus())) {
            throw new RuntimeException("只能删除待审核或已拒绝状态的补考记录");
        }
        
        try {
            makeupExamRepository.deleteById(makeupExamId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}