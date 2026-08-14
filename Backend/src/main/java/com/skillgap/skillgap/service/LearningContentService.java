package com.skillgap.skillgap.service;

import com.skillgap.skillgap.repository.LearningContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.skillgap.skillgap.entity.LearningContent;
@Service
public class LearningContentService {

    @Autowired
    private LearningContentRepository repository;

    public List<String> getTopics(Long skillId) {

        List<LearningContent> list = repository.findBySkillSkillId(skillId);

        System.out.println("DATA FROM DB: " + list.size()); // 🔥 ADD THIS

        return list.stream()
                .map(lc -> lc.getTopic())
                .toList();
    }
}
