package com.example.smartcampus.repository;

import com.example.smartcampus.entity.MakeupExam;
import com.example.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MakeupExamRepository extends JpaRepository<MakeupExam, Long> {

    /**
     * 根据学生ID查找补考记录
     */
    List<MakeupExam> findByStudentId(Long studentId);

    /**
     * 根据学号查找补考记录
     */
    @Query("SELECT m FROM MakeupExam m WHERE m.student.studentId = :studentId")
    List<MakeupExam> findByStudentStudentId(@Param("studentId") String studentId);

    /**
     * 根据教师ID查找补考记录
     */
    List<MakeupExam> findByTeacherId(Long teacherId);

    /**
     * 根据状态查找补考记录
     */
    List<MakeupExam> findByStatus(String status);

    /**
     * 根据学期查找补考记录
     */
    List<MakeupExam> findBySemester(String semester);

    /**
     * 根据学生ID和状态查找补考记录
     */
    List<MakeupExam> findByStudentIdAndStatus(Long studentId, String status);

    /**
     * 根据教师ID和状态查找补考记录
     */
    List<MakeupExam> findByTeacherIdAndStatus(Long teacherId, String status);

    /**
     * 根据学生ID和学期查找补考记录
     */
    List<MakeupExam> findByStudentIdAndSemester(Long studentId, String semester);

    /**
     * 根据学号和学期查找补考记录
     */
    @Query("SELECT m FROM MakeupExam m WHERE m.student.studentId = :studentId AND m.semester = :semester")
    List<MakeupExam> findByStudentStudentIdAndSemester(@Param("studentId") String studentId, @Param("semester") String semester);

    /**
     * 根据原始成绩ID查找补考记录
     */
    Optional<MakeupExam> findByOriginalGradeId(Long originalGradeId);

    /**
     * 根据课程代码和学期查找补考记录
     */
    List<MakeupExam> findByCourseCodeAndSemester(String courseCode, String semester);

    /**
     * 查找某学生在某学期的所有补考记录
     */
    @Query("SELECT m FROM MakeupExam m WHERE m.student.id = :studentId AND m.semester = :semester")
    List<MakeupExam> findStudentMakeupExamsBySemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    /**
     * 查找某教师教授的某门课程在某学期的所有补考记录
     */
    @Query("SELECT m FROM MakeupExam m WHERE m.teacher.id = :teacherId AND m.courseCode = :courseCode AND m.semester = :semester")
    List<MakeupExam> findTeacherCourseMakeupExamsBySemester(@Param("teacherId") Long teacherId, 
                                                           @Param("courseCode") String courseCode, 
                                                           @Param("semester") String semester);

    /**
     * 统计某学生的补考次数
     */
    @Query("SELECT COUNT(m) FROM MakeupExam m WHERE m.student.id = :studentId AND m.semester = :semester")
    Integer countMakeupExamsByStudentAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);
}