package com.assignment.jira.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskCommentDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String commentText;
    private LocalDateTime createdAt;
}
