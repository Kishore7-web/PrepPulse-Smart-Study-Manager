package com.preppulse.dto;

public record DashboardStats(long totalTasks, long completedTasks, long pendingTasks,
                             int completionPercent, long overdueTasks, int plannedMinutes) {}
