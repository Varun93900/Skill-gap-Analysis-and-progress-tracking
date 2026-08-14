package com.skillgap.skillgap.controller;

import com.skillgap.skillgap.dto.*;
import com.skillgap.skillgap.entity.JobRole;
import com.skillgap.skillgap.entity.JobRoleSkill;
import com.skillgap.skillgap.entity.User;
import com.skillgap.skillgap.entity.UserSkill;
import com.skillgap.skillgap.repository.JobRoleSkillRepository;
import com.skillgap.skillgap.repository.UserRepository;
import com.skillgap.skillgap.security.JwtUtil;
import com.skillgap.skillgap.service.*;
import com.skillgap.skillgap.util.OtpUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserSkillService userSkillService;
    private final SkillGapService skillGapService;

    private final RoadmapService roadmapService;
    private final  JwtUtil jwtService;
    private final JobRoleService jobRoleService;
    private final UserRepository userRepository;
    @Autowired
    private LearningContentService learningContentService;

    @Autowired
    private JobRoleSkillRepository jobRoleSkillRepository;

    public UserController(UserService userService,
                          JwtUtil jwtUtil,
                          UserSkillService userSkillService,
                          SkillGapService skillGapService,
                          RoadmapService roadmapService,
                          JwtUtil jwtService,
                          JobRoleService jobRoleService,
                          UserRepository userRepository) {

        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userSkillService = userSkillService;
        this.skillGapService = skillGapService;
        this.roadmapService = roadmapService;
        this. jwtService= jwtService;
        this. jobRoleService= jobRoleService;
        this.userRepository=userRepository;

    }

    @GetMapping("/test")
    public String test() {
        return "JWT is working";
    }
    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody RegisterUserRequest request) {
        System.out.println("REGISTER API HIT");
        User savedUser = userService.registerUser(request);
        savedUser.setPassword(null);
        return savedUser;
    }
    @PutMapping("/skills/{skillId}/complete")
    public ResponseEntity<String> markSkillCompleted(
            @PathVariable Long skillId,
            Authentication authentication) {

        String email = authentication.getName();

        userSkillService.markSkillCompleted(email, skillId);

        return ResponseEntity.ok("Skill marked as completed");
    }
    @PostMapping("/login")
    public String userLogin(@RequestBody LoginRequest request) {

        User user = userService.loginUser(request.getEmail(), request.getPassword());

        if (!user.getRole().equals("USER")) {
            throw new RuntimeException("Not a user account");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole());
    }
    @PostMapping("/skills")
    public UserSkill addUserSkill(@RequestBody CreateUserSkillRequest request,
                                  Authentication authentication) {

        String email = authentication.getName();

        return userSkillService.addUserSkillByEmail(
                email,
                request.getSkillId(),
                request.getLevel()
        );
    }
    @GetMapping("/skill-gap")
    public SkillGapResponse getSkillGap(
            Authentication authentication,
            @RequestParam Long roleId) {

        String email = authentication.getName();

        return skillGapService.analyzeSkillGapByEmail(email, roleId);
    }
    @GetMapping("/roadmap")
    public List<RoadmapResponse> getRoadmap(@RequestParam Long roleId) {

        List<JobRoleSkill> roleSkills =
                jobRoleSkillRepository.findByJobRoleRoleId(roleId);

        return roleSkills.stream()
                .map(jrs -> new RoadmapResponse(
                        jrs.getSkill().getSkillId(),     // Long
                        jrs.getSkill().getSkillName(),   // String
                        jrs.getPriority()                // String
                ))
                .toList();
    }
    @PutMapping("/skills/{skillId}/incomplete")
    public ResponseEntity<?> markSkillIncomplete(
            @PathVariable Long skillId,
            Authentication authentication) {

        String email = authentication.getName();

        userService.markSkillIncomplete(email, skillId);

        return ResponseEntity.ok("Skill marked as incomplete");
    }
    @GetMapping("/progress")
    public ProgressResponse getProgress(Authentication authentication) {

        String email = authentication.getName();

        return userSkillService.getUserProgress(email);
    }
    @GetMapping("/completed-skills")
    public List<String> getCompletedSkills(Authentication authentication) {

        String email = authentication.getName();

        return userSkillService.getCompletedSkills(email);
    }
    @GetMapping("/remaining-skills")
    public List<String> getRemainingSkills(Authentication authentication) {

        String email = authentication.getName();

        return userSkillService.getRemainingSkills(email);
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        return userService.verifyOtp(email, otp);
    }
    @GetMapping("/skills")
    public List<UserSkill> getUserSkills(Authentication authentication) {

        String email = authentication.getName();

        return userService.getUserSkillsByEmail(email);
    }
    @GetMapping("/roles")
    public List<JobRole> getAllRoles() {
        return jobRoleService.getAllJobRoles();
    }
    @GetMapping("/learning-content")
    public List<String> getLearningContent(@RequestParam Long skillId) {
        return learningContentService.getTopics(skillId);
    }
    @PostMapping("/add-skill")
    public ResponseEntity<?> addSkill(
            @RequestBody Map<String, String> request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String skillName = request.get("skillName");

        userSkillService.addSkillToUser(email, skillName);

        return ResponseEntity.ok("Skill added");
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Email already verified");
        }

        String otp = OtpUtil.generateOtp();

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        // 🔥 THIS IS THE MOST IMPORTANT LINE
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP resent successfully");
    }

}