package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
        name = "timetables",
        uniqueConstraints = {
                // 同一教室 + 同一时间段只能一条
                @UniqueConstraint(columnNames = {
                        "classroom_id", "day_of_week", "start_time", "end_time"
                })
        }
)
@Getter
@Setter
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 对应课程 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /** 上课教室 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    /** 星期几 */
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    /** 开始时间 */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /** 结束时间 */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}

