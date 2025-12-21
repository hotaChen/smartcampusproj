package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Grade;
import com.example.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 根据学生ID查找成绩
     */
    List<Grade> findByStudentId(Long studentId);

    /**
     * 根据教师ID查找成绩
     */
    List<Grade> findByTeacherId(Long teacherId);

    /**
     * 根据课程代码查找成绩
     */
    List<Grade> findByCourseCode(String courseCode);

    /**
     * 根据学期查找成绩
     */
    List<Grade> findBySemester(String semester);

    /**
     * 根据学生ID和学期查找成绩
     */
    List<Grade> findByStudentIdAndSemester(Long studentId, String semester);

    /**
     * 根据教师ID和学期查找成绩
     */
    List<Grade> findByTeacherIdAndSemester(Long teacherId, String semester);

    /**
     * 根据学生ID和课程代码查找成绩
     */
    List<Grade> findByStudentIdAndCourseCode(Long studentId, String courseCode);

    /**
     * 根据学生ID、课程代码和考试类型查找成绩
     */
    Optional<Grade> findByStudentIdAndCourseCodeAndExamType(Long studentId, String courseCode, String examType);

    /**
     * 根据学生ID、课程代码、考试类型和学期查找成绩
     */
    Optional<Grade> findByStudentIdAndCourseCodeAndExamTypeAndSemester(Long studentId, String courseCode, String examType, String semester);

    /**
     * 查找某学生在某学期的所有成绩
     */
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester AND g.status = 1")
    List<Grade> findStudentGradesBySemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    /**
     * 查找某教师教授的某门课程在某学期的所有成绩
     */
    @Query("SELECT g FROM Grade g WHERE g.teacher.id = :teacherId AND g.courseCode = :courseCode AND g.semester = :semester AND g.status = 1")
    List<Grade> findTeacherCourseGradesBySemester(@Param("teacherId") Long teacherId, 
                                                   @Param("courseCode") String courseCode, 
                                                   @Param("semester") String semester);

    /**
     * 统计某学生的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester AND g.status = 1")
    Float calculateAverageScoreByStudentAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    /**
     * 统计某课程的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.courseCode = :courseCode AND g.semester = :semester AND g.status = 1")
    Float calculateAverageScoreByCourseAndSemester(@Param("courseCode") String courseCode, @Param("semester") String semester);
}