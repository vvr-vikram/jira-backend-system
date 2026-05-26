package com.assignment.jira.controller;

import com.assignment.jira.dto.ApiResponse;
import com.assignment.jira.dto.ProjectDto;
import com.assignment.jira.dto.ProjectRequest;
import com.assignment.jira.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectDto project = projectService.createProject(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Project created successfully", project), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> getProject(@PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Project fetched successfully", project));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(new ApiResponse<>(true, "Projects fetched successfully", projects));
    }
}
