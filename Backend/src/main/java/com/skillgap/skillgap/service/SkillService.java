package com.skillgap.skillgap.service;

import com.skillgap.skillgap.entity.Skill;
import com.skillgap.skillgap.entity.SkillCategory;
import com.skillgap.skillgap.repository.SkillCategoryRepository;
import com.skillgap.skillgap.repository.SkillRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillCategoryRepository skillCategoryRepository;

    public SkillService(SkillRepository skillRepository,
                        SkillCategoryRepository skillCategoryRepository) {
        this.skillRepository = skillRepository;
        this.skillCategoryRepository = skillCategoryRepository;
    }

    public Skill createSkill(String skillName, Long categoryId) {

        SkillCategory category = skillCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Skill skill = new Skill();
        skill.setSkillName(skillName);
        skill.setCategory(category);

        return skillRepository.save(skill);
    }
}