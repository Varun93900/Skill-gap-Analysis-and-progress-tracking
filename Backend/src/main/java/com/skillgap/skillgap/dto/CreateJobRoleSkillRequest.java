package com.skillgap.skillgap.dto;

public class CreateJobRoleSkillRequest {

    private Long roleId;
    private Long skillId;
    private String priority;

    public Long getRoleId() {
        return roleId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public String getPriority() {
        return priority;
    }
}