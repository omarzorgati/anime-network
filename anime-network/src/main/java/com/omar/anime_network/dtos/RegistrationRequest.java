package com.omar.anime_network.dtos;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = " firstname is mandatory")
    @NotBlank(message = "firstname must not be blank")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "email is not formatted")
    private String email;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "Password must be between 8 and 20 characters and contain at least one uppercase letter and one number"
    )
    private String password;
    private String repeatPassword;
}
