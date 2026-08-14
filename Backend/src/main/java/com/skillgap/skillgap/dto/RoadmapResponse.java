package com.skillgap.skillgap.dto;

public class RoadmapResponse {

    private Long skillId;
    private String skillName;
    private String priority;

    public RoadmapResponse(Long skillId, String skillName, String priority) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.priority = priority;
    }

    public Long getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getPriority() {
        return priority;
    }
}