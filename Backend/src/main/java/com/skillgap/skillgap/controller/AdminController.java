package com.skillgap.skillgap.controller;

import com.skillgap.skillgap.dto.*;
import com.skillgap.skillgap.entity.*;
import com.skillgap.skillgap.repository.SkillCategoryRepository;
import com.skillgap.skillgap.repository.SkillRepository;
import com.skillgap.skillgap.security.JwtUtil;
import com.skillgap.skillgap.service.*;
import jakarta.validation.Valid;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final SkillCategoryService skillCategoryService;
    private final SkillService skillService;

    private final JobRoleService jobRoleService;
    private final JobRoleSkillService jobRoleSkillService;
    private final SkillGapService skillGapService;
    private final  UserService userService;
    private final JwtUtil jwtService;
    @Autowired
    private SkillCategoryRepository skillCategoryRepository;
    @Autowired
    private SkillRepository skillRepository;

    public AdminController(
            SkillCategoryService skillCategoryService,
            SkillService skillService,
            JobRoleService jobRoleService,
             JobRoleSkillService jobRoleSkillService,
            SkillGapService skillGapService, UserService userService,JwtUtil jwtService


    ) {
        this.skillCategoryService = skillCategoryService;
        this.skillService = skillService;
        this.jobRoleService = jobRoleService;
        this.jobRoleSkillService = jobRoleSkillService;
        this.skillGapService = skillGapService;
        this.userService= userService;
        this.jwtService=jwtService;


    }

    @PostMapping("/create-admin")
    public User createAdmin(@Valid @RequestBody CreateAdminRequest request) {

        User admin = userService.createAdmin(request);
        admin.setPassword(null);

        return admin;
    }
    @PostMapping("/login")
    public String adminLogin(@RequestBody LoginRequest request) {

        User user = userService.loginUser(request.getEmail(), request.getPassword());

        if (!user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Not an admin account");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole());
    }

    @PostMapping("/job-roles")
    public JobRole createJobRole(@RequestBody JobRole jobRole) {
        return jobRoleService.createJobRole(jobRole);
    }
    @PostMapping("/job-role-skills")
    public JobRoleSkill addSkillToRole(@RequestBody CreateJobRoleSkillRequest request) {

        return jobRoleSkillService.addSkillToRole(
                request.getRoleId(),
                request.getSkillId(),
                request.getPriority()
        );
    }
    @GetMapping("/job-roles")
    public List<JobRole> getAllJobRoles() {
        return jobRoleService.getAllJobRoles();
    }
    @PostMapping("/skills")
    public Skill createSkill(@RequestBody CreateSkillRequest request) {

        return skillService.createSkill(
                request.getSkillName(),
                request.getCategoryId()
        );
    }
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillRepository.findAll());
    }
    @PostMapping("/categories")
    public SkillCategory createCategory(@RequestBody SkillCategory category) {
        System.out.println(" category api hit");    
        return skillCategoryService.createCategory(category);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<SkillCategory>> getAllCategories() {
        return ResponseEntity.ok(skillCategoryRepository.findAll());
    }

    @GetMapping("/check")
    public String adminCheck() {
        return "Admin access granted";
    }
    @GetMapping("/skill-gap")
    public SkillGapResponse getSkillGap(
            @RequestParam Long userId,
            @RequestParam Long roleId) {

        return skillGapService.analyzeSkillGap(userId, roleId);
    }
}