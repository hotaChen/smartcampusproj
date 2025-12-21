package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;      // 课程代码

    @Column(nullable = false)
    private String courseName;      // 课程名称

    @Column(nullable = false)
    private LocalDateTime createTime; // 创建时间

    private Integer credit;         // 学分

    @Column(nullable = false)
    private String examType;        // 考试类型: 期中, 期末, 补考

    @Column(nullable = false)
    private String gradeLevel;      // 成绩等级: A, B, C, D, F

    @Column(nullable = false)
    private Float score;            // 分数

    @Column(nullable = false)
    private String semester;        // 学期: 2023-2024-1

    @Column(nullable = false)
    private Integer status;         // 状态: 0-无效, 1-有效

    @Column(nullable = false)
    private LocalDateTime updateTime; // 更新时间

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;          // 学生

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;          // 教师

    private Float finalScore;      // 期末成绩

    private Float midtermScore;     // 期中成绩

    private Float regularScore;     // 平时成绩

    @Column(length = 255)
    private String remark;          // 备注

    private Float totalScore;       // 总成绩

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;          // 课程

    private Float grade;            // 绩点

    @Column(length = 1000)
    private String comment;         // 评语

    // 构造函数
    public Grade() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = 1; // 默认有效
    }

    public Grade(String courseCode, String courseName, String examType, Float score, 
                 String semester, User student, User teacher) {
        this();
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.examType = examType;
        this.score = score;
        this.semester = semester;
        this.student = student;
        this.teacher = teacher;
    }
}