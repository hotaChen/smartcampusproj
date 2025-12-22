package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Classroom;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.repository.TimetableRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.TeacherTimetableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherTimetableServiceImpl implements TeacherTimetableService {

    private final TimetableRepository timetableRepo;
    private final UserRepository userRepo;

    public TeacherTimetableServiceImpl(
            TimetableRepository timetableRepo,
            UserRepository userRepo) {
        this.timetableRepo = timetableRepo;
        this.userRepo = userRepo;
    }
    @Override
    public List<TimetableDTO> getMyTimetable(Long teacherId) {
        return timetableRepo
                .findByCourseTeacherIdOrderByDayOfWeekAscStartTimeAsc(teacherId)
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

}

