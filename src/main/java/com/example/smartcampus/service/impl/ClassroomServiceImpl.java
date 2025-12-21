package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Classroom;
import com.example.smartcampus.repository.ClassroomRepository;
import com.example.smartcampus.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Classroom create(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom update(Long id, Classroom classroom) {
        Classroom existing = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        existing.setBuilding(classroom.getBuilding());
        existing.setRoomNumber(classroom.getRoomNumber());
        existing.setCapacity(classroom.getCapacity());

        return classroomRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        classroomRepository.deleteById(id);
    }

    @Override
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }
}
