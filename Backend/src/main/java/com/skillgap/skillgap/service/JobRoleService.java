package com.skillgap.skillgap.service;

import com.skillgap.skillgap.entity.JobRole;
import com.skillgap.skillgap.repository.JobRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;

    public JobRoleService(JobRoleRepository jobRoleRepository) {
        this.jobRoleRepository = jobRoleRepository;
    }

    public JobRole createJobRole(JobRole jobRole) {
        return jobRoleRepository.save(jobRole);
    }

    public List<JobRole> getAllJobRoles() {
        return jobRoleRepository.findAll();
    }
}