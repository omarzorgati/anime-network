package com.omar.anime_network.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.omar.anime_network.entities.AnimeSeason;
import com.omar.anime_network.entities.AnimeStatus;
import com.omar.anime_network.entities.AnimeType;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class AnimeResponse {
    private String title;
    private String description;
    private String author;
    private String seasonNumber;
    private String images;
    private AnimeSeason season;
    private AnimeType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date releaseDate;
    private AnimeStatus status;
    private Set<String> categoryNames;
    private int likes;
}

