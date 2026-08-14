package com.skillgap.skillgap.repository;

import com.skillgap.skillgap.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    long countByUserUserId(Long userId);

    long countByUserUserIdAndCompletedTrue(Long userId);

    Optional<UserSkill> findByUserUserIdAndSkillSkillId(Long userId, Long skillId);

    List<UserSkill> findByUserUserIdAndCompletedTrue(Long userId);

    List<UserSkill> findByUserUserId(Long userId);
    List<UserSkill> findByUserUserIdAndCompletedFalse(Long userId);
    boolean existsByUserUserIdAndSkillSkillId(Long userId, Long skillId);


    @Modifying
    @Query("UPDATE UserSkill us SET us.completed = true WHERE us.user.userId = :userId AND us.skill.skillId = :skillId")
    void markSkillAsComplete(Long userId, Long skillId);



}