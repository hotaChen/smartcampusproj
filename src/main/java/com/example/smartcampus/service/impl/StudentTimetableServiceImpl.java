package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.repository.TimetableRepository;
import com.example.smartcampus.service.StudentTimetableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentTimetableServiceImpl
        implements StudentTimetableService {

    private final TimetableRepository timetableRepository;

    public StudentTimetableServiceImpl(
            TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public List<TimetableDTO> getMyTimetable(Long studentId) {
        return timetableRepository
                .findByStudentIdOrderByDayOfWeekAscStartTimeAsc(studentId)
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

