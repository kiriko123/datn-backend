package com.example.demospringsecurity.service;

import com.example.demospringsecurity.dto.request.user.UserRegisterRequestDTO;
import com.example.demospringsecurity.dto.request.user.VerifyUserRequestDTO;
import com.example.demospringsecurity.dto.response.user.UserResponse;
import com.example.demospringsecurity.exception.InvalidDataException;
import com.example.demospringsecurity.exception.ResourceNotFoundException;
import com.example.demospringsecurity.model.User;
import com.example.demospringsecurity.repository.RoleRepository;
import com.example.demospringsecurity.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse register(UserRegisterRequestDTO userRegisterRequestDTO) {
        if (userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
            throw new InvalidDataException("Email already exists");
        }
        User user = User.builder()
                .email(userRegisterRequestDTO.getEmail())
                .name(userRegisterRequestDTO.getName())
                .password(passwordEncoder.encode(userRegisterRequestDTO.getPassword()))
                .role(roleRepository.findByName("ROLE_USER"))
                .verificationCode(this.generateVerificationCode())
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                .enabled(false)
                .build();

        UserResponse userResponse = UserResponse.fromUserToUserResponse(userRepository.save(user));

        this.sendVerificationEmail(user);

        return userResponse;
    }

    public void verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user != null) {
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (user.isEnabled()) {
                throw new InvalidDataException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }


    public void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationLink = "http://localhost:8080/api/v1/auth/verify?code=" + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #e9ecef;\">"
                + "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">"
                + "<h2 style=\"color: #333; text-align: center;\">Welcome to Our Application!</h2>"
                + "<p style=\"font-size: 16px; color: #555; text-align: center;\">To complete your registration, please click the link provided below:</p>"
                + "<div style=\"margin: 20px 0; padding: 20px; background-color: #f8f9fa; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); text-align: center;\">"
                + "<h3 style=\"color: #333; margin-bottom: 10px;\">Verification link:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff; margin: 0;\">" + verificationLink + "</p>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #888; text-align: center;\">If you did not request this verification, please disregard this email.</p>"
                + "<p style=\"font-size: 14px; color: #888; text-align: center;\">Thank you for using our application!</p>"
                + "</div>"
                + "</body>"
                + "</html>";


        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
