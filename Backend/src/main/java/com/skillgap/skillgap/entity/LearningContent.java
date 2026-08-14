package com.skillgap.skillgap.entity;

import com.skillgap.skillgap.entity.Skill;
import jakarta.persistence.*;

@Entity
@Table(name = "learning_content")
public class LearningContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private String topic;

    public Long getId() { return id; }
    public Skill getSkill() { return skill; }
    public String getTopic() { return topic; }

    public void setId(Long id) { this.id = id; }
    public void setSkill(Skill skill) { this.skill = skill; }
    public void setTopic(String topic) { this.topic = topic; }
}
