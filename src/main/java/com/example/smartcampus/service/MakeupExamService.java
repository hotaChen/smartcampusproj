package com.example.smartcampus.service;

import com.example.smartcampus.dto.MakeupExamApprovalRequest;
import com.example.smartcampus.dto.MakeupExamGradeRequest;
import com.example.smartcampus.dto.MakeupExamRequest;
import com.example.smartcampus.entity.MakeupExam;

import java.util.List;
import java.util.Optional;

public interface MakeupExamService {

    /**
     * 学生申请补考
     * @param makeupExamRequest 补考申请请求
     * @param studentId 学号
     * @return 创建的补考记录
     */
    MakeupExam applyForMakeupExam(MakeupExamRequest makeupExamRequest, String studentId);

    /**
     * 教师审批补考申请
     * @param makeupExamId 补考记录ID
     * @param approvalRequest 审批请求
     * @param teacherId 教师ID
     * @return 更新后的补考记录
     */
    MakeupExam approveMakeupExam(Long makeupExamId, MakeupExamApprovalRequest approvalRequest, Long teacherId);

    /**
     * 教师录入补考成绩
     * @param makeupExamId 补考记录ID
     * @param gradeRequest 成绩请求
     * @param teacherId 教师ID
     * @return 更新后的补考记录
     */
    MakeupExam enterMakeupExamGrade(Long makeupExamId, MakeupExamGradeRequest gradeRequest, Long teacherId);

    /**
     * 根据ID获取补考记录
     * @param makeupExamId 补考记录ID
     * @return 补考记录
     */
    Optional<MakeupExam> getMakeupExamById(Long makeupExamId);

    /**
     * 获取学生的所有补考记录
     * @param studentId 学生ID
     * @return 补考记录列表
     */
    List<MakeupExam> getMakeupExamsByStudentId(Long studentId);

    /**
     * 根据学号获取学生的所有补考记录
     * @param studentId 学号
     * @return 补考记录列表
     */
    List<MakeupExam> getMakeupExamsByStudentStudentId(String studentId);

    /**
     * 获取教师审批的所有补考记录
     * @param teacherId 教师ID
     * @return 补考记录列表
     */
    List<MakeupExam> getMakeupExamsByTeacherId(Long teacherId);

    /**
     * 获取学生在某学期的补考记录
     * @param studentId 学生ID
     * @param semester 学期
     * @return 补考记录列表
     */
    List<MakeupExam> getStudentMakeupExamsBySemester(Long studentId, String semester);

    /**
     * 根据学号获取学生在某学期的补考记录
     * @param studentId 学号
     * @param semester 学期
     * @return 补考记录列表
     */
    List<MakeupExam> getStudentMakeupExamsByStudentIdAndSemester(String studentId, String semester);

    /**
     * 根据课程代码和学期获取补考记录
     * @param courseCode 课程代码
     * @param semester 学期
     * @return 补考记录列表
     */
    List<MakeupExam> getMakeupExamsByCourseCodeAndSemester(String courseCode, String semester);

    /**
     * 获取所有补考记录
     * @return 所有补考记录列表
     */
    List<MakeupExam> getAllMakeupExams();

    /**
     * 获取教师教授的某门课程在某学期的所有补考记录
     * @param teacherId 教师ID
     * @param courseCode 课程代码
     * @param semester 学期
     * @return 补考记录列表
     */
    List<MakeupExam> getTeacherCourseMakeupExamsBySemester(Long teacherId, String courseCode, String semester);

    /**
     * 获取所有待审批的补考申请
     * @return 待审批的补考申请列表
     */
    List<MakeupExam> getPendingMakeupExams();

    /**
     * 删除补考记录
     * @param makeupExamId 补考记录ID
     * @param teacherId 教师ID
     * @return 是否删除成功
     */
    boolean deleteMakeupExam(Long makeupExamId, Long teacherId);
}