package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "makeup_exams")
public class MakeupExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;      // 课程代码

    @Column(nullable = false)
    private String courseName;      // 课程名称

    @Column(nullable = false)
    private LocalDateTime createTime; // 创建时间

    @Column(nullable = false)
    private LocalDateTime examDate;  // 考试日期

    @Column(nullable = false)
    private String examLocation;    // 考试地点

    @Column(nullable = false)
    private String originalGrade;   // 原始成绩等级

    @Column(nullable = false)
    private String semester;        // 学期: 2023-2024-1

    @Column(nullable = false)
    private String status;          // 状态: 待审核, 已批准, 已拒绝, 已完成

    @Column(nullable = false)
    private LocalDateTime updateTime; // 更新时间

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;          // 学生

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;          // 教师

    @Column(length = 1000)
    private String reason;         // 补考原因

    @Column(name = "apply_reason", length = 255)
    private String applyReason;    // 申请原因

    private LocalDateTime applyTime; // 申请时间

    @Column(name = "approval_remark", length = 255)
    private String approvalRemark;  // 审批备注

    private LocalDateTime approvalTime; // 审批时间

    @Column(length = 255)
    private String gradeLevel;     // 补考成绩等级

    private Float score;           // 补考分数

    @Column(name = "makeup_grade", length = 255)
    private String makeupGrade;    // 补考成绩等级

    @Column(nullable = false)
    private Long originalGradeId;  // 原始成绩ID

    @Column(length = 1000)
    private String comment;        // 评语

    // 构造函数
    public MakeupExam() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = "待审核";
    }

    public MakeupExam(String courseCode, String courseName, LocalDateTime examDate, 
                      String examLocation, String originalGrade, String semester, 
                      User student, User teacher, Long originalGradeId) {
        this();
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.examDate = examDate;
        this.examLocation = examLocation;
        this.originalGrade = originalGrade;
        this.semester = semester;
        this.student = student;
        this.teacher = teacher;
        this.originalGradeId = originalGradeId;
    }
}