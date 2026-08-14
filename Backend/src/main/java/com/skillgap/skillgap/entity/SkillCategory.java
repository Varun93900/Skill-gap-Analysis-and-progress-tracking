package com.skillgap.skillgap.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skill_categories")
public class SkillCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true,nullable = false)
    private String categoryName;

    public SkillCategory(){
    }

    public SkillCategory(String categoryName){
        this.categoryName = categoryName;
    }

    public Long getCategoryId(){
        return categoryId;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
