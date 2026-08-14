package com.skillgap.skillgap.entity;
import  jakarta.persistence.*;
@Entity
@Table(name = "Skills")

public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @Column(unique = true,nullable = false)
    private String skillName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SkillCategory category;

    public Skill(){

    }

    public Skill(String skillName, SkillCategory category){
        this.skillName=skillName;
        this.category=category;
    }
    public Long getSkillId(){
        return skillId;
    }

    public String getSkillName(){
        return skillName;
    }
    public void setSkillName(String skillName){
        this.skillName=skillName;
    }

    public SkillCategory getCategory(){
        return category;
    }

    public void setCategory(SkillCategory category){
        this.category=category;
    }


}
