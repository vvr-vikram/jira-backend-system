package com.assignment.jira.dto;

import com.assignment.jira.entity.TaskPriority;
import com.assignment.jira.entity.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private String projectName;
    private Long assignedUserId;
    private String assignedUserName;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}
