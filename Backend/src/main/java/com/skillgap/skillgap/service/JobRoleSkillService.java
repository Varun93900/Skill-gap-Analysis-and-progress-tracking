package com.skillgap.skillgap.service;

import com.skillgap.skillgap.entity.JobRole;
import com.skillgap.skillgap.entity.JobRoleSkill;
import com.skillgap.skillgap.entity.Skill;
import com.skillgap.skillgap.repository.JobRoleRepository;
import com.skillgap.skillgap.repository.JobRoleSkillRepository;
import com.skillgap.skillgap.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleSkillService {

    private final JobRoleSkillRepository jobRoleSkillRepository;
    private final JobRoleRepository jobRoleRepository;
    private final SkillRepository skillRepository;

    public JobRoleSkillService(JobRoleSkillRepository jobRoleSkillRepository,
                               JobRoleRepository jobRoleRepository,
                               SkillRepository skillRepository) {
        this.jobRoleSkillRepository = jobRoleSkillRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.skillRepository = skillRepository;
    }

    public JobRoleSkill addSkillToRole(Long roleId, Long skillId, String priority) {

        JobRole role = jobRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        JobRoleSkill jobRoleSkill = new JobRoleSkill();
        jobRoleSkill.setJobRole(role);
        jobRoleSkill.setSkill(skill);
        jobRoleSkill.setPriority(priority);

        return jobRoleSkillRepository.save(jobRoleSkill);
    }

    public List<JobRoleSkill> getSkillsForRole(Long roleId) {
        return jobRoleSkillRepository.findByJobRoleRoleId(roleId);
    }
}