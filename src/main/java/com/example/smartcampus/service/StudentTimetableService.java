package com.example.smartcampus.service;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;

import java.util.List;

public interface StudentTimetableService {

    List<TimetableDTO> getMyTimetable(Long studentId);
}

