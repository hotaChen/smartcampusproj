package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.CourseDTO;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.entity.CourseSelection;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.CourseRepository;
import com.example.smartcampus.repository.CourseSelectionRepository;
import com.example.smartcampus.repository.TimetableRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.CourseSelectionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CourseSelectionServiceImpl implements CourseSelectionService {

    private final CourseSelectionRepository selectionRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final TimetableRepository timetableRepo;

    public CourseSelectionServiceImpl(
            CourseSelectionRepository selectionRepo,
            CourseRepository courseRepo,
            UserRepository userRepo,
            TimetableRepository timetableRepo
    ) {
        this.selectionRepo = selectionRepo;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.timetableRepo=timetableRepo;
    }

    @Override
    public void selectCourse(Long studentId, Long courseId) {

        // 1. 校验学生
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        if (!"STUDENT".equals(student.getUserType())) {
            throw new RuntimeException("只有学生可以选课");
        }

        // 2. 校验课程
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        // 3. 防重复选课
        if (selectionRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("已选该课程");
        }

        // 4. 容量校验
        long selectedCount = selectionRepo.countByCourseId(courseId);
        if (selectedCount >= course.getCapacity()) {
            throw new RuntimeException("课程已满");
        }

        // 5. 时间冲突校验（新增）
        List<Timetable> newCourseTimetables =
                timetableRepo.findByCourseId(courseId);

        for (Timetable t : newCourseTimetables) {
            boolean conflict =
                    timetableRepo.existsStudentTimeConflict(
                            studentId,
                            t.getDayOfWeek(),
                            t.getStartTime(),
                            t.getEndTime()
                    );

            if (conflict) {
                throw new RuntimeException("选课失败：课程时间冲突");
            }
        }

        // 6. 创建选课记录
        CourseSelection selection = new CourseSelection();
        selection.setStudent(student);
        selection.setCourse(course);
        selection.setSelectedAt(LocalDateTime.now());

        selectionRepo.save(selection);
    }


    @Override
    public void dropCourse(Long studentId, Long courseId) {
        CourseSelection selection = selectionRepo
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("未选该课程"));

        selectionRepo.delete(selection);
    }

    @Override
    public List<CourseDTO> getMyCourses(Long studentId) {
        return selectionRepo.findByStudentId(studentId)
                .stream()
                .map(selection -> {
                    Course c = selection.getCourse();
                    Long teacherId = c.getTeacher() != null ? c.getTeacher().getId() : null;
                    String teacherName = c.getTeacher() != null ? c.getTeacher().getRealName() : "-";
                    Long classroomId = c.getClassroom() != null ? c.getClassroom().getId() : null;
                    String classroomName = c.getClassroom() != null ? c.getClassroom().getBuilding() + "-" + c.getClassroom().getRoomNumber() : "-";

                    return new CourseDTO(
                            c.getId(),
                            c.getCourseCode(),
                            c.getName(),
                            c.getCredit(),
                            c.getCapacity(),
                            teacherId,
                            teacherName,
                            classroomId,
                            classroomName
                    );
                })
                .toList();
    }


}

