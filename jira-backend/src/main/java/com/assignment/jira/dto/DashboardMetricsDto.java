package com.assignment.jira.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardMetricsDto {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long blockedTasks;
    private double projectProgressPercentage;
}
