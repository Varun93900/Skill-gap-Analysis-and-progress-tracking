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
public class SkillGapService {

    private final JobRoleSkillRepository jobRoleSkillRepository;
    private final UserSkillRepository userSkillRepository;
    @Autowired
    private UserRepository userRepository;

    public SkillGapService(JobRoleSkillRepository jobRoleSkillRepository,
                           UserSkillRepository userSkillRepository) {
        this.jobRoleSkillRepository = jobRoleSkillRepository;
        this.userSkillRepository = userSkillRepository;
    }
    public SkillGapResponse analyzeSkillGap(Long userId, Long roleId) {

        List<JobRoleSkill> requiredSkills =
                jobRoleSkillRepository.findByJobRoleRoleId(roleId);

        List<UserSkill> userSkills =
                userSkillRepository.findByUserUserId(userId);

        Map<Long, String> userSkillMap = new HashMap<>();

        for (UserSkill us : userSkills) {
            userSkillMap.put(us.getSkill().getSkillId(), us.getLevel());
        }

        List<String> missingSkills = new ArrayList<>();
        List<String> weakSkills = new ArrayList<>();
        List<String> strongSkills = new ArrayList<>();

        for (JobRoleSkill rs : requiredSkills) {

            Long skillId = rs.getSkill().getSkillId();
            String skillName = rs.getSkill().getSkillName();

            if (!userSkillMap.containsKey(skillId)) {
                missingSkills.add(skillName);
            } else {
                String level = userSkillMap.get(skillId);

                if (level.equals("BEGINNER")) {
                    weakSkills.add(skillName);
                } else {
                    strongSkills.add(skillName);
                }
            }
        }

        return new SkillGapResponse(strongSkills, weakSkills, missingSkills);
    }
    public SkillGapResponse analyzeSkillGapByEmail(String email, Long roleId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return analyzeSkillGap(user.getUserId(), roleId);
    }
    public List<RoadmapResponse> generateRoadmap(Long roleId) {
        List<JobRoleSkill> roleSkills =
                jobRoleSkillRepository.findByJobRoleRoleId(roleId);

        Set<Long> seenSkills = new HashSet<>();
        List<RoadmapResponse> roadmap = new ArrayList<>();

        for (JobRoleSkill rs : roleSkills) {

            Long skillId = rs.getSkill().getSkillId();

            if (seenSkills.contains(skillId)) continue; // 🚀 remove duplicates

            seenSkills.add(skillId);

            roadmap.add(new RoadmapResponse(
                    skillId,
                    rs.getSkill().getSkillName(),
                    rs.getPriority()
            ));
        }

        return roadmap;
    }
}