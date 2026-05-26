package com.assignment.jira.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskActivityDto {
    private Long id;
    private Long taskId;
    private String activityType;
    private String oldValue;
    private String newValue;
    private Long updatedById;
    private String updatedByName;
    private LocalDateTime timestamp;
}
