package com.skillgap.skillgap.repository;

import com.skillgap.skillgap.entity.JobRoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRoleSkillRepository extends JpaRepository<JobRoleSkill, Long> {

    List<JobRoleSkill> findByJobRoleRoleId(Long roleId);

}
