package com.assignment.jira.controller;

import com.assignment.jira.dto.*;
import com.assignment.jira.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@Valid @RequestBody TaskRequest request,
                                                           @RequestParam Long createdByUserId) {
        TaskDto task = taskService.createTask(request, createdByUserId);
        return new ResponseEntity<>(new ApiResponse<>(true, "Task created successfully", task), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(@PathVariable Long id,
                                                           @Valid @RequestBody TaskRequest request,
                                                           @RequestParam Long updatedByUserId) {
        TaskDto task = taskService.updateTask(id, request, updatedByUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task updated successfully", task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDto>> getTask(@PathVariable Long id) {
        TaskDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task fetched successfully", task));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<Page<TaskDto>>> getTasksByProject(@PathVariable Long projectId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.getTasksByProjectId(projectId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks fetched successfully", tasks));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<TaskDto>>> getTasksByUser(@PathVariable Long userId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.getTasksByUserId(userId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks fetched successfully", tasks));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TaskDto>>> searchTasks(@RequestParam String keyword,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.searchTasks(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks fetched successfully", tasks));
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<TaskCommentDto>> addComment(@PathVariable Long taskId,
                                                                  @Valid @RequestBody TaskCommentRequest request) {
        TaskCommentDto comment = taskService.addComment(taskId, request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Comment added successfully", comment), HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<List<TaskCommentDto>>> getTaskComments(@PathVariable Long taskId) {
        List<TaskCommentDto> comments = taskService.getTaskComments(taskId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comments fetched successfully", comments));
    }

    @GetMapping("/{taskId}/activities")
    public ResponseEntity<ApiResponse<List<TaskActivityDto>>> getTaskActivities(@PathVariable Long taskId) {
        List<TaskActivityDto> activities = taskService.getTaskActivities(taskId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Activities fetched successfully", activities));
    }

    @GetMapping("/metrics/{projectId}")
    public ResponseEntity<ApiResponse<DashboardMetricsDto>> getDashboardMetrics(@PathVariable Long projectId) {
        DashboardMetricsDto metrics = taskService.getDashboardMetrics(projectId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard metrics fetched successfully", metrics));
    }
}
