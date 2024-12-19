package com.omar.anime_network.dtos;

import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
                message = "Password must be between 8 and 20 characters and contain at least one uppercase letter and one number"
        )
        String password,
        String repeatPassword) {
}

