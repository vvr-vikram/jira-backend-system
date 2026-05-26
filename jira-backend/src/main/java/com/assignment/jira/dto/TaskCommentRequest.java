package com.assignment.jira.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCommentRequest {
    @NotBlank(message = "Comment text is required")
    private String commentText;

    @NotNull(message = "User ID is required")
    private Long userId;
}
