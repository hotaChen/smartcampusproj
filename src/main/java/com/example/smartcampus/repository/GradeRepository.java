package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Grade;
import com.example.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.smartcampus.entity.Grade;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 根据学生ID查找成绩
     */
    List<Grade> findByStudentId(Long studentId);

    /**
     * 根据学号查找成绩
     */
    @Query("SELECT g FROM Grade g WHERE g.student.studentId = :studentId")
    List<Grade> findByStudentStudentId(@Param("studentId") String studentId);

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
     * 根据学号和学期查找成绩
     */
    @Query("SELECT g FROM Grade g WHERE g.student.studentId = :studentId AND g.semester = :semester AND g.status = 1")
    List<Grade> findByStudentStudentIdAndSemester(@Param("studentId") String studentId, @Param("semester") String semester);

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
    // 计算平均成绩
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.score IS NOT NULL")
    Double calculateAverageGrade();

    // 计算通过的成绩数量（假设60分以上为通过）
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.score >= 60")
    Long countPassedGrades();

    // 获取课程成绩统计
    @Query("SELECT c.name, AVG(g.score), COUNT(g.student), SUM(CASE WHEN g.score >= 60 THEN 1 ELSE 0 END) " +
            "FROM Grade g JOIN g.course c GROUP BY c.id, c.name")
    List<Object[]> findCourseGradeStatistics();

    /**
     * 根据学号和学期计算学生的平均分
     * @param studentNumber 学号
     * @param semester 学期
     * @return 平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.studentId = :studentNumber AND g.semester = :semester")
    Float calculateAverageScoreByStudentNumberAndSemester(@Param("studentNumber") String studentNumber, @Param("semester") String semester);

    /**
     * 根据学号和学期统计学生的课程数量
     * @param studentNumber 学号
     * @param semester 学期
     * @return 课程数量
     */
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.studentId = :studentNumber AND g.semester = :semester")
    Long countCoursesByStudentNumberAndSemester(@Param("studentNumber") String studentNumber, @Param("semester") String semester);

    /**
     * 根据学号和学期获取学生的最高分
     * @param studentNumber 学号
     * @param semester 学期
     * @return 最高分
     */
    @Query("SELECT MAX(g.score) FROM Grade g WHERE g.student.studentId = :studentNumber AND g.semester = :semester")
    Float findMaxScoreByStudentNumberAndSemester(@Param("studentNumber") String studentNumber, @Param("semester") String semester);

    /**
     * 根据学号和学期获取学生的最低分
     * @param studentNumber 学号
     * @param semester 学期
     * @return 最低分
     */
    @Query("SELECT MIN(g.score) FROM Grade g WHERE g.student.studentId = :studentNumber AND g.semester = :semester")
    Float findMinScoreByStudentNumberAndSemester(@Param("studentNumber") String studentNumber, @Param("semester") String semester);

}