package com.skillgap.skillgap.service;

import com.skillgap.skillgap.dto.ProgressResponse;
import com.skillgap.skillgap.entity.Skill;
import com.skillgap.skillgap.entity.User;
import com.skillgap.skillgap.entity.UserSkill;
import com.skillgap.skillgap.repository.SkillRepository;
import com.skillgap.skillgap.repository.UserRepository;
import com.skillgap.skillgap.repository.UserSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserSkillService(UserSkillRepository userSkillRepository,
                            UserRepository userRepository,
                            SkillRepository skillRepository) {

        this.userSkillRepository = userSkillRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public UserSkill addUserSkill(Long userId, Long skillId, String level) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setLevel(level);

        return userSkillRepository.save(userSkill);
    }
    public void markSkillCompleted(String email, Long skillId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserSkill> optionalUserSkill =
                userSkillRepository.findByUserUserIdAndSkillSkillId(
                        user.getUserId(), skillId
                );

        if (optionalUserSkill.isPresent()) {
            // ✅ Already exists → just mark complete
            UserSkill userSkill = optionalUserSkill.get();
            userSkill.setCompleted(true);
            userSkillRepository.save(userSkill);

        } else {
            // ✅ NOT EXISTS → create new entry
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Skill not found"));

            UserSkill newSkill = new UserSkill();
            newSkill.setUser(user);
            newSkill.setSkill(skill);
            newSkill.setLevel("BEGINNER");
            newSkill.setCompleted(true);

            userSkillRepository.save(newSkill);
        }
    }
    public List<UserSkill> getUserSkills(Long userId) {
        return userSkillRepository.findByUserUserId(userId);
    }
    public ProgressResponse getUserProgress(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getUserId();

        long total = userSkillRepository.countByUserUserId(userId);
        long completed = userSkillRepository.countByUserUserIdAndCompletedTrue(userId);

        long remaining = total - completed;

        int progress = total == 0 ? 0 : (int) ((completed * 100) / total);

        return new ProgressResponse(total, completed, remaining, progress);
    }
    public List<String> getCompletedSkills(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<UserSkill> completedSkills =
                userSkillRepository.findByUserUserIdAndCompletedTrue(user.getUserId());

        return completedSkills.stream()
                .map(us -> us.getSkill().getSkillName())
                .toList();
    }
    public List<String> getRemainingSkills(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<UserSkill> remainingSkills =
                userSkillRepository.findByUserUserIdAndCompletedFalse(user.getUserId());

        return remainingSkills.stream()
                .map(us -> us.getSkill().getSkillName())
                .toList();
    }
    public UserSkill addUserSkillByEmail(String email, Long skillId, String level) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addUserSkill(user.getUserId(), skillId, level);
    }

    public void addSkillToUser(String email, String skillName) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Skill skill = skillRepository.findBySkillName(skillName)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        // prevent duplicate
        boolean exists = userSkillRepository
                .existsByUserUserIdAndSkillSkillId(
                        user.getUserId(),
                        skill.getSkillId()
                );

        if (exists) return;

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setCompleted(true);
        userSkill.setLevel("BEGINNER");

        userSkillRepository.save(userSkill);
    }
}