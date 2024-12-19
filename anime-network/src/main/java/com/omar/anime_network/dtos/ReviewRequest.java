package com.omar.anime_network.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "you need to write a comment")
    private String comment;

    @Min(1)
    @Max(5)
    private float rating;

}
