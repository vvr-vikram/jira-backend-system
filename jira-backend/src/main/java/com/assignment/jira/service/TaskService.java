package com.assignment.jira.service;

import com.assignment.jira.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskRequest request, Long createdByUserId);
    TaskDto updateTask(Long taskId, TaskRequest request, Long updatedByUserId);
    TaskDto getTaskById(Long taskId);
    Page<TaskDto> getTasksByProjectId(Long projectId, Pageable pageable);
    Page<TaskDto> getTasksByUserId(Long userId, Pageable pageable);
    Page<TaskDto> searchTasks(String keyword, Pageable pageable);
    
    TaskCommentDto addComment(Long taskId, TaskCommentRequest request);
    List<TaskCommentDto> getTaskComments(Long taskId);
    
    List<TaskActivityDto> getTaskActivities(Long taskId);
    DashboardMetricsDto getDashboardMetrics(Long projectId);
}
