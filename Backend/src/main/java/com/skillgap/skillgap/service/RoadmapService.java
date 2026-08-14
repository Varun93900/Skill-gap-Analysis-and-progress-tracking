package com.skillgap.skillgap.service;

import com.skillgap.skillgap.dto.RoadmapResponse;
import com.skillgap.skillgap.dto.SkillGapResponse;
import com.skillgap.skillgap.entity.JobRoleSkill;
import com.skillgap.skillgap.entity.User;
import com.skillgap.skillgap.entity.UserSkill;
import com.skillgap.skillgap.repository.JobRoleSkillRepository;
import com.skillgap.skillgap.repository.UserRepository;
import com.skillgap.skillgap.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class  RoadmapService {

    private final SkillGapService skillGapService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRoleSkillRepository jobRoleSkillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    public RoadmapService(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }

    public List<RoadmapResponse> generateRoadmapByEmail(String email, Long roleId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateRoadmap(user.getUserId(), roleId);
    }

    public List<RoadmapResponse> generateRoadmap(Long roleId, Long userId) {

        List<JobRoleSkill> roleSkills =
                jobRoleSkillRepository.findByJobRoleRoleId(roleId);

        List<UserSkill> userSkills =
                userSkillRepository.findByUserUserId(userId);

        Set<Long> userSkillIds = new HashSet<>();

        for (UserSkill us : userSkills) {
            userSkillIds.add(us.getSkill().getSkillId());
        }

        List<RoadmapResponse> roadmap = new ArrayList<>();

        for (JobRoleSkill rs : roleSkills) {

            Long skillId = rs.getSkill().getSkillId();

            // ✅ ONLY ADD MISSING SKILLS
            if (!userSkillIds.contains(skillId)) {
                roadmap.add(new RoadmapResponse(
                        skillId,
                        rs.getSkill().getSkillName(),
                        rs.getPriority()
                ));
            }
        }

        // ✅ SORT BY PRIORITY
        roadmap.sort((a, b) -> {
            Map<String, Integer> order = Map.of(
                    "HIGH", 1,
                    "MEDIUM", 2,
                    "LOW", 3
            );
            return order.get(a.getPriority()) - order.get(b.getPriority());
        });

        return roadmap;
    }
}