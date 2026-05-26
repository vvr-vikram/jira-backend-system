package com.assignment.jira.service.impl;

import com.assignment.jira.dto.*;
import com.assignment.jira.entity.*;
import com.assignment.jira.exception.ResourceNotFoundException;
import com.assignment.jira.repository.*;
import com.assignment.jira.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskActivityRepository taskActivityRepository;

    @Override
    public TaskDto createTask(TaskRequest request, Long createdByUserId) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
        }
        User creator = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .project(project)
                .assignedUser(assignedUser)
                .dueDate(request.getDueDate())
                .build();
        
        task = taskRepository.save(task);
        logActivity(task, "CREATED", null, task.getStatus().name(), creator);
        return mapToDto(task);
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskRequest request, Long updatedByUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        User updater = userRepository.findById(updatedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Updater user not found"));

        if (!task.getStatus().equals(request.getStatus())) {
            logActivity(task, "STATUS_UPDATED", task.getStatus().name(), request.getStatus().name(), updater);
        }
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        if (request.getAssignedUserId() != null) {
            if (task.getAssignedUser() == null || !task.getAssignedUser().getId().equals(request.getAssignedUserId())) {
                User assignedUser = userRepository.findById(request.getAssignedUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
                task.setAssignedUser(assignedUser);
                logActivity(task, "ASSIGNEE_UPDATED", task.getAssignedUser() != null ? task.getAssignedUser().getName() : "None", assignedUser.getName(), updater);
            }
        } else {
            task.setAssignedUser(null);
        }

        task = taskRepository.save(task);
        return mapToDto(task);
    }

    @Override
    public TaskDto getTaskById(Long taskId) {
        return mapToDto(taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found")));
    }

    @Override
    public Page<TaskDto> getTasksByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable).map(this::mapToDto);
    }

    @Override
    public Page<TaskDto> getTasksByUserId(Long userId, Pageable pageable) {
        return taskRepository.findByAssignedUserId(userId, pageable).map(this::mapToDto);
    }

    @Override
    public Page<TaskDto> searchTasks(String keyword, Pageable pageable) {
        return taskRepository.searchTasks(keyword, pageable).map(this::mapToDto);
    }

    @Override
    public TaskCommentDto addComment(Long taskId, TaskCommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskComment comment = TaskComment.builder()
                .task(task)
                .user(user)
                .commentText(request.getCommentText())
                .build();
        comment = taskCommentRepository.save(comment);

        return TaskCommentDto.builder()
                .id(comment.getId())
                .taskId(task.getId())
                .userId(user.getId())
                .userName(user.getName())
                .commentText(comment.getCommentText())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    public List<TaskCommentDto> getTaskComments(Long taskId) {
        return taskCommentRepository.findByTaskId(taskId).stream()
                .map(comment -> TaskCommentDto.builder()
                        .id(comment.getId())
                        .taskId(comment.getTask().getId())
                        .userId(comment.getUser().getId())
                        .userName(comment.getUser().getName())
                        .commentText(comment.getCommentText())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskActivityDto> getTaskActivities(Long taskId) {
        return taskActivityRepository.findByTaskIdOrderByTimestampDesc(taskId).stream()
                .map(activity -> TaskActivityDto.builder()
                        .id(activity.getId())
                        .taskId(activity.getTask().getId())
                        .activityType(activity.getActivityType())
                        .oldValue(activity.getOldValue())
                        .newValue(activity.getNewValue())
                        .updatedById(activity.getUpdatedBy().getId())
                        .updatedByName(activity.getUpdatedBy().getName())
                        .timestamp(activity.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DashboardMetricsDto getDashboardMetrics(Long projectId) {
        long totalTasks = taskRepository.countByProjectId(projectId);
        long completedTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.DONE);
        long pendingTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.OPEN) +
                            taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.IN_PROGRESS);
        long blockedTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.BLOCKED);

        double progress = 0.0;
        if (totalTasks > 0) {
            progress = (double) completedTasks / totalTasks * 100;
        }

        return DashboardMetricsDto.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .blockedTasks(blockedTasks)
                .projectProgressPercentage(Math.round(progress * 100.0) / 100.0)
                .build();
    }

    private void logActivity(Task task, String type, String oldValue, String newValue, User updatedBy) {
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .activityType(type)
                .oldValue(oldValue)
                .newValue(newValue)
                .updatedBy(updatedBy)
                .build();
        taskActivityRepository.save(activity);
    }

    private TaskDto mapToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .assignedUserId(task.getAssignedUser() != null ? task.getAssignedUser().getId() : null)
                .assignedUserName(task.getAssignedUser() != null ? task.getAssignedUser().getName() : null)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
