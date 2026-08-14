package com.skillgap.skillgap.service;

import com.skillgap.skillgap.entity.SkillCategory;
import com.skillgap.skillgap.repository.SkillCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillCategoryService {

    private final SkillCategoryRepository skillCategoryRepository;

    public SkillCategoryService(SkillCategoryRepository skillCategoryRepository) {
        this.skillCategoryRepository = skillCategoryRepository;
    }

    public SkillCategory createCategory(SkillCategory category) {
        return skillCategoryRepository.save(category);
    }
}