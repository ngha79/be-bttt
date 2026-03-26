package com.example.bttt.dto;

import com.example.bttt.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long deadline;
    private Long userId;
    private String username;
    private Long projectId;
    private String projectName;
    private Long createdAt;
    private Long updatedAt;
}
