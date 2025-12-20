package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface TimetableRepository
        extends JpaRepository<Timetable, Long> {

    /** 教室时间冲突检测 */
    @Query("""
        select count(t) > 0 from Timetable t
        where t.classroom.id = :classroomId
          and t.dayOfWeek = :dayOfWeek
          and t.startTime < :endTime
          and t.endTime > :startTime
    """)
    boolean existsClassroomConflict(
            Long classroomId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    @Query("""
    select t from Timetable t
    where t.course.id in (
        select cs.course.id
        from CourseSelection cs
        where cs.student.id = :studentId
    )
""")
    List<Timetable> findStudentTimetable(Long studentId);

    @Query("""
    select count(t) > 0 from Timetable t
    where t.course.id in (
        select cs.course.id
        from CourseSelection cs
        where cs.student.id = :studentId
    )
      and t.dayOfWeek = :dayOfWeek
      and t.startTime < :endTime
      and t.endTime > :startTime
""")
    boolean existsStudentTimeConflict(
            Long studentId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );


    @Query("""
        select t from Timetable t
        where t.course.teacher.id = :teacherId
        order by t.dayOfWeek, t.startTime
    """)
    List<Timetable> findTeacherTimetable(Long teacherId);



    /** 课程自身时间冲突 */
    boolean existsByCourseIdAndDayOfWeekAndStartTimeAndEndTime(
            Long courseId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    List<Timetable> findByCourseId(Long courseId);

    List<Timetable> findByCourseTeacherIdOrderByDayOfWeekAscStartTimeAsc(Long teacherId);

    @Query("""
        SELECT t 
        FROM Timetable t 
        JOIN CourseSelection cs ON cs.course = t.course
        WHERE cs.student.id = :studentId
        ORDER BY t.dayOfWeek ASC, t.startTime ASC
    """)
    List<Timetable> findByStudentIdOrderByDayOfWeekAscStartTimeAsc(@Param("studentId") Long studentId);
}

