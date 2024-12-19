package com.omar.anime_network.controllers;


import com.omar.anime_network.dtos.AuthenticationRequest;
import com.omar.anime_network.dtos.AuthenticationResponse;
import com.omar.anime_network.dtos.ChangePasswordRequest;
import com.omar.anime_network.dtos.RegistrationRequest;
import com.omar.anime_network.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
//from open api
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.ok("Registration successful! Please check your email.");
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @PostMapping("/verify-mail/{email}")
    public ResponseEntity<String> verifyMail(
            @PathVariable("email") @Email(message = "Invalid email format") String email
    ) throws MessagingException {
        authenticationService.verifyMail(email);
        return ResponseEntity.ok("Email sent for verification");
    }
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(
            @PathVariable("otp") String  otp,
            @PathVariable("email") String email
    ){
        authenticationService.verifyOtp(otp,email);
        return ResponseEntity.ok("OTP verified");
    }

    @PutMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            @PathVariable("email") String email
    ){
        authenticationService.changePassword(changePasswordRequest,email);
        return ResponseEntity.ok("Password changed");
    }


}
