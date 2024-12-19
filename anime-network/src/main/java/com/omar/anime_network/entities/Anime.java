package com.omar.anime_network.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String seasonNumber;
    @Column(length = 1024)
    private String images;
    @Enumerated(EnumType.STRING)
    private AnimeSeason season;
    @Enumerated(EnumType.STRING)
    private AnimeType type;
    private Date releaseDate;
    @Enumerated(EnumType.STRING)
    private AnimeStatus status;
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false, updatable = false)
    private Date createdDate;
    @Column(nullable = false)
    private int likes = 0;

    @OneToMany(mappedBy = "anime",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set <Reviews> reviews = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "anime_category",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

}
