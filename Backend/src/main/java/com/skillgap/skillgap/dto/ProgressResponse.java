package com.skillgap.skillgap.dto;

public class ProgressResponse {

    private long completedSkills;
    private long totalSkills;
    private int progressPercentage;
    private long remaining;

    public ProgressResponse(long totalSkills, long completedSkills, long remaining, int progressPercentage) {
        this.totalSkills = totalSkills;
        this.completedSkills = completedSkills;
        this.remaining = remaining;
        this.progressPercentage = progressPercentage;
    }

    public long getCompletedSkills() {
        return completedSkills;
    }

    public long getTotalSkills() {
        return totalSkills;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }
}