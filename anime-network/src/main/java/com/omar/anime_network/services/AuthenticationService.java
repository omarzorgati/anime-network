package com.omar.anime_network.services;


import com.omar.anime_network.dtos.*;
import com.omar.anime_network.entities.*;
import com.omar.anime_network.exceptionHandler.customExceptions.InvalidOtpException;
import com.omar.anime_network.exceptionHandler.customExceptions.OtpExpiredException;
import com.omar.anime_network.exceptionHandler.customExceptions.PasswordsDoNotMatchException;
import com.omar.anime_network.repositories.ForgotPasswordRepository;
import com.omar.anime_network.repositories.RoleRepository;
import com.omar.anime_network.repositories.TokenRepository;
import com.omar.anime_network.repositories.UserRepository;
import com.omar.anime_network.security.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.USER, null));
        }

        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.ADMIN, null));
        }
    }
    public void register(RegistrationRequest request) throws MessagingException {
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        var userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("User role not found"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .role(userRole)
                .build();
        userRepository.save(user);
        sendApplicationConfirmationEmail(user);

    }
    private void sendApplicationConfirmationEmail(User user) {
        String userEmail = user.getEmail();
        String subject = "Your application has been submitted";
        String text = String.format("Dear %s,\n\nThank you for registering on Anime Network. Your account has been created successfully.\n\nBest regards,\nAnime Network Team",
                user.fullName());

        MailBody mailBody = MailBody.builder()
                .to(userEmail)
                .subject(subject)
                .text(text)
                .build();

        mailService.sendSimpleMessage(mailBody);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims,user);
        //we need to revoke all tokens before getting a new one
        revokeAllUserTokens(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    private void saveUserToken(User user,String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void verifyMail(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide valid email!"));
        int otp = otpGenerator(); // Generate integer OTP
        String hashedOtp = passwordEncoder.encode(String.valueOf(otp)); // Hash the OTP

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your password reset: " + otp) // Send plain OTP to the user
                .subject("OTP for Password Reset")
                .build();

        Optional<ForgotPassword> existingFp = forgotPasswordRepository.findByUser(user);
        ForgotPassword fp;
        if (existingFp.isPresent()) {
            fp = existingFp.get();
            fp.setOtp(hashedOtp); // Save hashed OTP
            fp.setExpirationTime(new Date(System.currentTimeMillis() + 600 * 1000)); // 10 minutes
        } else {
            fp = ForgotPassword.builder()
                    .otp(hashedOtp) // Save hashed OTP
                    .expirationTime(new Date(System.currentTimeMillis() + 600 * 1000)) // 10 minutes
                    .user(user)
                    .build();
        }
        mailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generates a 6-digit OTP
    }

    public void verifyOtp(String otp, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide valid email!"));
        ForgotPassword fp = forgotPasswordRepository.findByUser(user)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            throw new OtpExpiredException("OTP has expired");
        }

        // Compare the provided OTP with the hashed OTP
        if (!passwordEncoder.matches(otp, fp.getOtp())) {
            throw new InvalidOtpException("Invalid OTP for email: " + email);
        }


    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, String email) {
        if (!Objects.equals(changePasswordRequest.password(), changePasswordRequest.repeatPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.password());
        userRepository.updatePassword(email, encodedPassword);
    }



}
