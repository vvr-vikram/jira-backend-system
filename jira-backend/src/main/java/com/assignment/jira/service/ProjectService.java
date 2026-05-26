package com.assignment.jira.service;

import com.assignment.jira.dto.ProjectDto;
import com.assignment.jira.dto.ProjectRequest;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectRequest request);
    ProjectDto getProjectById(Long id);
    List<ProjectDto> getAllProjects();
}
