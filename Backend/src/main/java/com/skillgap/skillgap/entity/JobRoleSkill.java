package com.skillgap.skillgap.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_role_skills")
public class JobRoleSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private JobRole jobRole;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private String priority;

    public Long getId() {
        return id;
    }

    public JobRole getJobRole() {
        return jobRole;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}