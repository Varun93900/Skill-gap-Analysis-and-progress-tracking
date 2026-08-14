package com.skillgap.skillgap.service;

import com.skillgap.skillgap.dto.CreateAdminRequest;
import com.skillgap.skillgap.dto.RegisterUserRequest;
import com.skillgap.skillgap.entity.User;
import com.skillgap.skillgap.entity.UserSkill;
import com.skillgap.skillgap.exception.EmailAlreadyExistsException;
import com.skillgap.skillgap.exception.InvalidPasswordException;
import com.skillgap.skillgap.exception.UserNotFoundException;
import com.skillgap.skillgap.repository.UserRepository;
import com.skillgap.skillgap.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.skillgap.skillgap.util.OtpUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserSkillRepository userSkillRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService, UserSkillRepository userSkillRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userSkillRepository=userSkillRepository;

    }

    public User createAdmin(CreateAdminRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User registerUser(RegisterUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        String otp = OtpUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setEmailVerified(false);

        user = userRepository.save(user);

        emailService.sendOtp(user.getEmail(), otp);

        return user;
    }
    @Transactional
    public String verifyOtp(String email, String otp) {

        System.out.println("VERIFY API HIT"); // optional (for debug)

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null) {
            throw new RuntimeException("No OTP found. Please register again.");
        }

        if (!user.getOtp().trim().equals(otp.trim())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // ✅ MAIN FIX
        user.setEmailVerified(true);
        user.setOtp(null);          // 🔥 CLEAN OTP
        user.setOtpExpiry(null);    // 🔥 CLEAN EXPIRY

        userRepository.save(user);  // ✅ persist changes

        return "Email verified successfully";
    }

    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }
        return user;

    }
    public List<UserSkill> getUserSkillsByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userSkillRepository.findByUserUserId(user.getUserId());
    }
    public void markSkillIncomplete(String email, Long skillId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserSkill userSkill = userSkillRepository
                .findByUserUserIdAndSkillSkillId(user.getUserId(), skillId)
                .orElseThrow(() -> new RuntimeException("UserSkill not found"));

        userSkillRepository.delete(userSkill);
    }
    @Service
    public class UserCleanupService {

        @Autowired
        private UserRepository userRepository;

        @Scheduled(fixedRate = 60000) // every 1 minute
        public void removeUnverifiedUsers() {
            List<User> users = userRepository
                    .findByEmailVerifiedFalseAndOtpExpiryBefore(LocalDateTime.now());

            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                System.out.println("Deleted unverified users: " + users.size());
            }
        }
    }


}