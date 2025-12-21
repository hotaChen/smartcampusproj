package com.example.smartcampus.service;

import com.example.smartcampus.entity.Classroom;

import java.util.List;

public interface ClassroomService {

    Classroom create(Classroom classroom);

    Classroom update(Long id, Classroom classroom);

    void delete(Long id);

    List<Classroom> findAll();
}
