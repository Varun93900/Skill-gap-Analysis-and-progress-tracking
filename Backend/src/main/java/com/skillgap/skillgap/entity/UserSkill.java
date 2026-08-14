package com.skillgap.skillgap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "user_skills")
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private String level;

    @Column(name = "completed")
    private boolean completed = false;

    public UserSkill() {}

    public UserSkill(User user, Skill skill, String level) {
        this.user = user;
        this.skill = skill;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Skill getSkill() {
        return skill;
    }

    public String getLevel() {
        return level;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}