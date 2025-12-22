package com.example.smartcampus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class MakeupExamApprovalRequest {
    
    @NotBlank(message = "审批状态不能为空")
    private String status;  // 已批准, 已拒绝
    
    @Size(max = 255, message = "审批备注长度不能超过255字符")
    private String approvalRemark;
}