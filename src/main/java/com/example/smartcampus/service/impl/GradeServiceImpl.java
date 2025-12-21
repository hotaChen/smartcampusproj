package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.GradeEntryRequest;
import com.example.smartcampus.entity.Grade;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.GradeRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public GradeServiceImpl(GradeRepository gradeRepository, UserRepository userRepository) {
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Grade enterGrade(GradeEntryRequest gradeRequest, Long teacherId) {
        // 验证教师是否存在
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));

        // 验证学生是否存在
        User student = userRepository.findById(gradeRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        // 检查是否已存在相同学生、课程、考试类型和学期的成绩
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndCourseCodeAndExamTypeAndSemester(
                gradeRequest.getStudentId(), 
                gradeRequest.getCourseCode(), 
                gradeRequest.getExamType(), 
                gradeRequest.getSemester()
        );

        if (existingGrade.isPresent()) {
            throw new RuntimeException("该学生在此课程的" + gradeRequest.getExamType() + "成绩已存在");
        }

        // 创建成绩对象
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setTeacher(teacher);
        grade.setCourseCode(gradeRequest.getCourseCode());
        grade.setCourseName(gradeRequest.getCourseName());
        grade.setExamType(gradeRequest.getExamType());
        grade.setScore(gradeRequest.getScore());
        grade.setSemester(gradeRequest.getSemester());
        grade.setCredit(gradeRequest.getCredit());
        grade.setGradeLevel(gradeRequest.getGradeLevel());
        grade.setFinalScore(gradeRequest.getFinalScore());
        grade.setMidtermScore(gradeRequest.getMidtermScore());
        grade.setRegularScore(gradeRequest.getRegularScore());
        grade.setRemark(gradeRequest.getRemark());
        grade.setTotalScore(gradeRequest.getTotalScore());
        grade.setGrade(gradeRequest.getGrade());
        grade.setComment(gradeRequest.getComment());
        grade.setUpdateTime(LocalDateTime.now());

        return gradeRepository.save(grade);
    }

    @Override
    public Grade updateGrade(Long gradeId, GradeEntryRequest gradeRequest, Long teacherId) {
        // 验证成绩是否存在
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("成绩不存在"));

        // 验证是否是录入该成绩的教师或管理员
        if (!grade.getTeacher().getId().equals(teacherId) && 
            !"ADMIN".equals(userRepository.findById(teacherId).get().getUserType())) {
            throw new RuntimeException("无权限修改此成绩");
        }

        // 更新成绩信息
        grade.setScore(gradeRequest.getScore());
        grade.setGradeLevel(gradeRequest.getGradeLevel());
        grade.setFinalScore(gradeRequest.getFinalScore());
        grade.setMidtermScore(gradeRequest.getMidtermScore());
        grade.setRegularScore(gradeRequest.getRegularScore());
        grade.setRemark(gradeRequest.getRemark());
        grade.setTotalScore(gradeRequest.getTotalScore());
        grade.setGrade(gradeRequest.getGrade());
        grade.setComment(gradeRequest.getComment());
        grade.setUpdateTime(LocalDateTime.now());

        return gradeRepository.save(grade);
    }

    @Override
    public Optional<Grade> getGradeById(Long gradeId) {
        return gradeRepository.findById(gradeId);
    }

    @Override
    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @Override
    public List<Grade> getGradesByTeacherId(Long teacherId) {
        return gradeRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Grade> getStudentGradesBySemester(Long studentId, String semester) {
        return gradeRepository.findStudentGradesBySemester(studentId, semester);
    }

    @Override
    public List<Grade> getTeacherCourseGradesBySemester(Long teacherId, String courseCode, String semester) {
        return gradeRepository.findTeacherCourseGradesBySemester(teacherId, courseCode, semester);
    }

    @Override
    public boolean deleteGrade(Long gradeId, Long teacherId) {
        // 验证成绩是否存在
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("成绩不存在"));

        // 验证是否是录入该成绩的教师或管理员
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!grade.getTeacher().getId().equals(teacherId) && !"ADMIN".equals(teacher.getUserType())) {
            throw new RuntimeException("无权限删除此成绩");
        }

        // 软删除：设置状态为无效
        grade.setStatus(0);
        grade.setUpdateTime(LocalDateTime.now());
        gradeRepository.save(grade);
        
        return true;
    }

    @Override
    public List<Grade> batchEnterGrades(List<GradeEntryRequest> gradeRequests, Long teacherId) {
        List<Grade> grades = new ArrayList<>();
        
        for (GradeEntryRequest gradeRequest : gradeRequests) {
            try {
                Grade grade = enterGrade(gradeRequest, teacherId);
                grades.add(grade);
            } catch (Exception e) {
                // 记录错误，继续处理其他成绩
                System.err.println("批量录入成绩失败: " + e.getMessage());
            }
        }
        
        return grades;
    }

    @Override
    public Float calculateAverageScoreByStudentAndSemester(Long studentId, String semester) {
        Float avgScore = gradeRepository.calculateAverageScoreByStudentAndSemester(studentId, semester);
        return avgScore != null ? avgScore : 0.0f;
    }

    @Override
    public Float calculateAverageScoreByCourseAndSemester(String courseCode, String semester) {
        Float avgScore = gradeRepository.calculateAverageScoreByCourseAndSemester(courseCode, semester);
        return avgScore != null ? avgScore : 0.0f;
    }
}