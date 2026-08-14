package com.skillgap.skillgap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roadmapId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private JobRole jobRole;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @Column(nullable = false)
    private Double completionPercentage;

    public Roadmap() {
    }

    public Roadmap(User user, JobRole jobRole, LocalDateTime generatedDate, Double completionPercentage) {
        this.user = user;
        this.jobRole = jobRole;
        this.generatedDate = generatedDate;
        this.completionPercentage = completionPercentage;
    }

    public Long getRoadmapId() {
        return roadmapId;
    }

    public User getUser() {
        return user;
    }

    public JobRole getJobRole() {
        return jobRole;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public Double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
}