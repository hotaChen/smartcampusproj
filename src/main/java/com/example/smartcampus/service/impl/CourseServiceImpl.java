package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.CreateCourseRequest;
import com.example.smartcampus.entity.Classroom;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.ClassroomRepository;
import com.example.smartcampus.repository.CourseRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             ClassroomRepository classroomRepository,
                             UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;

    }

    @Override
    public Course createCourse(CreateCourseRequest req, CustomUserDetails userDetails) {

        User teacher;

        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            if (req.getTeacherId() == null) {
                throw new RuntimeException("管理员创建课程必须指定教师");
            }

            teacher = userRepository.findById(req.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("指定教师不存在"));

            if (!"TEACHER".equals(teacher.getUserType())) {
                throw new RuntimeException("指定用户不是教师");
            }

        } else {
            // 教师自己创建课程
            teacher = userDetails.getUser();
        }

        // 检查教室是否存在
        Classroom classroom = classroomRepository.findById(req.getClassroomId())
                .orElseThrow(() -> new RuntimeException("指定教室不存在"));

        Course course = new Course();
        course.setCourseCode(req.getCourseCode());
        course.setName(req.getName());
        course.setCredit(req.getCredit());
        course.setCapacity(req.getCapacity());
        course.setTeacher(teacher);
        course.setTeacherName(teacher.getRealName());
        course.setClassroom(classroom);

        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, Course course) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        // 更新教师，只接受 teacherId
        if (course.getTeacher() != null && course.getTeacher().getId() != null) {
            Long teacherId = course.getTeacher().getId();
            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("指定教师不存在"));
            if (!"TEACHER".equals(teacher.getUserType())) {
                throw new RuntimeException("指定用户不是教师");
            }
            existing.setTeacher(teacher);
            existing.setTeacherName(teacher.getRealName());
        }

        // 更新教室，只接受 classroomId
        if (course.getClassroom() != null && course.getClassroom().getId() != null) {
            Long classroomId = course.getClassroom().getId();
            Classroom classroom = classroomRepository.findById(classroomId)
                    .orElseThrow(() -> new RuntimeException("指定教室不存在"));
            existing.setClassroom(classroom);
        }

        existing.setCourseCode(course.getCourseCode());
        existing.setName(course.getName());
        existing.setCredit(course.getCredit());
        existing.setCapacity(course.getCapacity());

        return courseRepository.save(existing);
    }




    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override public List<Course> findAll() { return courseRepository.findAll(); }
}
