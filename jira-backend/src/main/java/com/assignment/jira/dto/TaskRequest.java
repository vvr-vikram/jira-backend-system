package com.assignment.jira.dto;

import com.assignment.jira.entity.TaskPriority;
import com.assignment.jira.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private TaskPriority priority;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assignedUserId;

    private LocalDate dueDate;
}
