package com.example.smartcampus.service;

import com.example.smartcampus.dto.GradeEntryRequest;
import com.example.smartcampus.dto.GradeReportDTO;
import com.example.smartcampus.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {

    /**
     * 录入成绩
     * @param gradeRequest 成绩录入请求
     * @param teacherId 教师ID
     * @return 录入的成绩
     */
    Grade enterGrade(GradeEntryRequest gradeRequest, Long teacherId);

    /**
     * 更新成绩
     * @param gradeId 成绩ID
     * @param gradeRequest 成绩更新请求
     * @param teacherId 教师ID
     * @return 更新后的成绩
     */
    Grade updateGrade(Long gradeId, GradeEntryRequest gradeRequest, Long teacherId);

    /**
     * 根据ID获取成绩
     * @param gradeId 成绩ID
     * @return 成绩信息
     */
    Optional<Grade> getGradeById(Long gradeId);

    /**
     * 获取学生的所有成绩
     * @param studentId 学生ID
     * @return 成绩列表
     */
    List<Grade> getGradesByStudentId(Long studentId);

    /**
     * 根据学号获取成绩列表
     * @param studentId 学号
     * @return 成绩列表
     */
    List<Grade> getGradesByStudentStudentId(String studentId);

    /**
     * 获取教师录入的所有成绩
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    List<Grade> getGradesByTeacherId(Long teacherId);

    /**
     * 获取学生在某学期的成绩
     * @param studentId 学生ID
     * @param semester 学期
     * @return 成绩列表
     */
    List<Grade> getStudentGradesBySemester(Long studentId, String semester);

    /**
     * 根据学号获取学生在某学期的成绩
     * @param studentId 学号
     * @param semester 学期
     * @return 成绩列表
     */
    List<Grade> getStudentGradesByStudentIdAndSemester(String studentId, String semester);

    /**
     * 获取教师教授的某门课程在某学期的所有成绩
     * @param teacherId 教师ID
     * @param courseCode 课程代码
     * @param semester 学期
     * @return 成绩列表
     */
    List<Grade> getTeacherCourseGradesBySemester(Long teacherId, String courseCode, String semester);

    /**
     * 删除成绩
     * @param gradeId 成绩ID
     * @param teacherId 教师ID
     * @return 是否删除成功
     */
    boolean deleteGrade(Long gradeId, Long teacherId);

    /**
     * 批量录入成绩
     * @param gradeRequests 成绩录入请求列表
     * @param teacherId 教师ID
     * @return 录入的成绩列表
     */
    List<Grade> batchEnterGrades(List<GradeEntryRequest> gradeRequests, Long teacherId);

    /**
     * 计算学生的平均分
     * @param studentId 学生ID
     * @param semester 学期
     * @return 平均分
     */
    Float calculateAverageScoreByStudentAndSemester(Long studentId, String semester);

    /**
     * 根据学号计算学生的平均分
     * @param studentNumber 学号
     * @param semester 学期
     * @return 平均分
     */
    Float calculateAverageScoreByStudentNumberAndSemester(String studentNumber, String semester);

    /**
     * 计算课程的平均分
     * @param courseCode 课程代码
     * @param semester 学期
     * @return 平均分
     */
    Float calculateAverageScoreByCourseAndSemester(String courseCode, String semester);
    
    /**
     * 生成学生成绩单
     * @param studentId 学生ID
     * @param semester 学期（可选，如果为空则生成所有学期的成绩单）
     * @return 成绩单数据
     */
    GradeReportDTO generateGradeReport(Long studentId, String semester);
}