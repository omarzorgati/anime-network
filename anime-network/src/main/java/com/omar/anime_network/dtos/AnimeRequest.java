package com.omar.anime_network.dtos;

import com.omar.anime_network.entities.AnimeSeason;
import com.omar.anime_network.entities.AnimeStatus;
import com.omar.anime_network.entities.AnimeType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimeRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 3, max = 20, message = "Author name must be between 3 and 20 characters")
    private String author;

    @NotBlank(message = "Season number is mandatory")
    @Pattern(regexp = "^\\d+$", message = "Season number must be a valid number")
    private String seasonNumber;

    @NotNull(message = "Season is mandatory")
    private AnimeSeason season;

    @NotNull(message = "Type is mandatory")
    private AnimeType type;

    @NotNull(message = "Release date is mandatory")
    @PastOrPresent(message = "Release date must be in the past or present")
    private Date releaseDate;

    @NotNull(message = "Status is mandatory")
    private AnimeStatus status;

    @NotEmpty(message = "At least one category name is required")
    private List<@NotBlank(message = "Category name cannot be blank") String> categoryNames;
}
