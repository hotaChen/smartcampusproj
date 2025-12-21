package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Classroom;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.repository.ClassroomRepository;
import com.example.smartcampus.repository.CourseRepository;
import com.example.smartcampus.repository.TimetableRepository;
import com.example.smartcampus.service.TimetableService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepo;
    private final CourseRepository courseRepo;
    private final ClassroomRepository classroomRepo;

    public TimetableServiceImpl(
            TimetableRepository timetableRepo,
            CourseRepository courseRepo,
            ClassroomRepository classroomRepo) {
        this.timetableRepo = timetableRepo;
        this.courseRepo = courseRepo;
        this.classroomRepo = classroomRepo;
    }

    @Override
    public Timetable createTimetable(
            Long courseId,
            Long classroomId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new RuntimeException("时间段非法");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        Classroom classroom = classroomRepo.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("教室不存在"));

        // 1️⃣ 教室冲突
        if (timetableRepo.existsClassroomConflict(
                classroomId, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("该教室在此时间段已有课程");
        }

        // 2️⃣ 课程自身冲突
        if (timetableRepo.existsByCourseIdAndDayOfWeekAndStartTimeAndEndTime(
                courseId, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("课程时间安排重复");
        }

        Timetable timetable = new Timetable();
        timetable.setCourse(course);
        timetable.setClassroom(classroom);
        timetable.setDayOfWeek(dayOfWeek);
        timetable.setStartTime(startTime);
        timetable.setEndTime(endTime);

        return timetableRepo.save(timetable);
    }

    @Override
    public List<TimetableDTO> getCourseTimetable(Long courseId) {
        return timetableRepo.findByCourseId(courseId)
                .stream()
                .map(t -> {
                    TimetableDTO dto = new TimetableDTO();
                    dto.setId(t.getId());
                    dto.setCourseId(t.getCourse().getId());
                    dto.setCourseName(t.getCourse().getName());
                    dto.setTeacherName(t.getCourse().getTeacherName());
                    dto.setClassroom(
                            t.getClassroom().getBuilding() + "-" +
                                    t.getClassroom().getRoomNumber()
                    );
                    dto.setDayOfWeek(t.getDayOfWeek());
                    dto.setStartTime(t.getStartTime());
                    dto.setEndTime(t.getEndTime());
                    return dto;
                })
                .toList();
    }
    @Override
    public Timetable updateTimetable(
            Long id,
            Long classroomId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    ) {
        Timetable t = timetableRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("课表不存在"));

        Classroom classroom = classroomRepo.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("教室不存在"));

        t.setClassroom(classroom);
        t.setDayOfWeek(dayOfWeek);
        t.setStartTime(startTime);
        t.setEndTime(endTime);

        return timetableRepo.save(t);
    }


    @Override
    public void deleteTimetable(Long id) {
        if (!timetableRepo.existsById(id)) {
            throw new RuntimeException("课表不存在");
        }
        timetableRepo.deleteById(id);
    }


}

