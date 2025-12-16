package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceType;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;


    @Column(nullable = false)
    private String status;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // ===== getters & setters =====

}

